package org.ossiaustria.amigobox.calls

import ProfileImage
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.AmigoThemeLight
import org.ossiaustria.amigobox.ui.commons.HomeButtonRow
import org.ossiaustria.amigobox.ui.commons.IconButtonSmall
import org.ossiaustria.amigobox.ui.commons.Toasts
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.enums.CallState
import org.ossiaustria.lib.domain.models.enums.CallType
import org.ossiaustria.lib.domain.services.CallEvent
import java.util.UUID.randomUUID

class CallFragment : Fragment() {

    private val callViewModel by viewModel<CallViewModel>()
    private val incomingEventsViewModel by viewModel<IncomingEventsViewModel>()

    val navigator: Navigator by inject()
    private val phoneSoundManager: PhoneSoundManager by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * Fragment will be started with Call for INCOMING Calls or
         * Person for OUTGOING Calls to be created with a provided Person
         */
        val call = Navigator.getCall(requireArguments())
        val person = Navigator.getPerson(requireArguments())

        if (call != null) {
            callViewModel.prepareIncomingCall(call)
            Navigator.setCall(requireArguments(), null)
        } else if (person != null) {
            callViewModel.createNewOutgoingCall(person)
            Navigator.setPerson(requireArguments(), null)
        } else {
            val call1 = callViewModel.state.value?.call
            Toast.makeText(context, "Call or Person are both null!", Toast.LENGTH_LONG).show()
        }

        /**
         * INCOMING & OUTGOING calls can change at any time whether by server
         * Timeout, Accept, Denying or Finishing or others
         */
        callViewModel.state.observe(viewLifecycleOwner) {
            if (it is CallViewState.Calling) {
                // play a Sound while Call is Calling and waiting
                if (it.outgoing) {
                    phoneSoundManager.playOutgoing()
                } else {
                    phoneSoundManager.playIncoming()
                }
            } else if (it is CallViewState.Accepted) {
                /**
                 * Call started, because it was ACCEPTED by others or you,
                 * And JitsiFragment must be created and started right now
                 */
                phoneSoundManager.stopAll()
                val token = callViewModel.getToken()
                if (token != null) {
                    navigator.toJitsiCall(it.call.id.toString(), token)
                } else {
                    Toasts.showLong(requireContext(), "Cannot use Call, no token ? ")
                }
            } else if (it is CallViewState.Finished) {
                phoneSoundManager.stopAll()
                Toasts.showLong(requireContext(), "Anruf beendet")
                navigator.back()
            } else {
                phoneSoundManager.stopAll()
            }
        }

        /**
         * Observe the FCM via "incomingEventsViewModel", needed for both INCOMING & OUTGOING
         * Could happen at any time, but usually will happen when the other party performs an operation
         * like: ACCEPT, CANCEL, DENY, FINISH
         *
         * This will use CallViewModel and trigger a state change, see callViewModel.state
         */
        incomingEventsViewModel.notifiedCall.observe(viewLifecycleOwner) {
            callViewModel.onObservedCallChanged(it)
        }

        /**
         * Observe the JitsiActivity via LocalBroadCastmanager, needed for both INCOMING & OUTGOING
         * Could happen at any time, but usually will happen when a change a Particiap Left oder joined,
         * or you terminate the JitsiFragment.
         *
         * Just Finishes the Call
         *
         * This will use CallViewModel and trigger a state change, see callViewModel.state
         */
        incomingEventsViewModel.notifiedCallEvent.observe(viewLifecycleOwner) {
            if (it == CallEvent.FINISHED) {
                callViewModel.finish()
            }
        }

        incomingEventsViewModel.viewModelScope.launch {
            phoneSoundManager.prepare(requireContext())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            AmigoThemeLight {
                val state by callViewModel.state.observeAsState(null)
                val partner by callViewModel.partner.observeAsState(null)
                if (partner != null && state != null) {
                    Surface(color = MaterialTheme.colors.secondary) {
                        CallFragmentComposable(
                            partner!!.name,
                            partner!!.absoluteAvatarUrl(),
                            state!!,
                            onAccept = callViewModel::accept,
                            onCancel = callViewModel::cancel,
                            onDeny = callViewModel::deny,
                            onFinish = callViewModel::finish,
                            onBack = ::back
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            state != null && partner == null -> Text("Person nicht erreichbar.")
                            state is CallViewState.Failure -> Text(text = (state as CallViewState.Failure).error.toString())
                            else -> Text("Loading..")
                        }
                    }
                }
            }
        }
    }

    fun back() {
        navigator.back()
    }

    override fun onDestroy() {
        super.onDestroy()
        phoneSoundManager.release()
    }
}

@Composable
fun CallFragmentComposable(
    partnerName: String,
    partnerAvatarUrl: String?,
    callViewState: CallViewState,
    onAccept: () -> Unit,
    onCancel: () -> Unit,
    onDeny: () -> Unit,
    onFinish: () -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        HomeButtonRow {
            IconButtonSmall(
                resourceId = R.drawable.ic_help_icon,
                backgroundColor = MaterialTheme.colors.secondary,
                fillColor = MaterialTheme.colors.primary
            ) {
                //TODO:Help screens
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(UIConstants.CallFragmentConstants.MIDDLE_ROW_HEIGHT)
        ) {
            ProfileImage(
                partnerAvatarUrl,
                contentScale = ContentScale.Crop
            )
        }

        Row(
            modifier = Modifier
                .height(UIConstants.CallFragmentConstants.BOTTOM_ROW_HEIGHT)
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.onSecondary),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .width(UIConstants.CallFragmentConstants.COLUMN_WIDTH)
                    .padding(
                        start = UIConstants.Defaults.OUTER_PADDING,
                        top = UIConstants.Defaults.INNER_PADDING
                    )
            ) {
                Text(
                    text = partnerName,
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.onPrimary
                )
                Text(
                    if (callViewState is CallViewState.Calling) {
                        if (callViewState.outgoing) stringResource(R.string.calling_text)
                        else stringResource(R.string.wants_to_talk_to_you_text)
                    } else if (callViewState is CallViewState.Finished) {
                        stringResource(R.string.call_finished)
                    } else if (callViewState is CallViewState.Cancelled) {
                        stringResource(R.string.call_cancelled)
                    } else if (callViewState is CallViewState.Timeout) {
                        stringResource(R.string.call_timeout)
                    } else if (callViewState is CallViewState.Failure) {
                        callViewState.error?.toString() ?: "Failure"
                    } else {
                        ""
                    },

                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onPrimary
                )
            }
            Column(
                modifier = Modifier
                    .width(UIConstants.CallFragmentConstants.COLUMN_WIDTH)
                    .padding(
                        top = UIConstants.Defaults.INNER_PADDING,
                        end = UIConstants.Defaults.INNER_PADDING,
                        start = UIConstants.CallFragmentConstants.COLUMN_START_PADDING
                    )
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (callViewState is CallViewState.Accepted) {
                        //finish the call
                        IconButtonSmall(
                            resourceId = R.drawable.ic_decline_call,
                            backgroundColor = MaterialTheme.colors.onSecondary,
                            fillColor = MaterialTheme.colors.error,
                        ) { onFinish() }
                        Text(
                            text = stringResource(R.string.tap_to) + "\n" + stringResource(R.string.end_the_call),
                            style = MaterialTheme.typography.h4,
                            color = MaterialTheme.colors.onPrimary,
                            textAlign = TextAlign.Center
                        )
                    } else if (callViewState is CallViewState.Calling) {
                        if (callViewState.outgoing) {
                            //cancel the call

                            IconButtonSmall(
                                resourceId = R.drawable.ic_decline_call,
                                backgroundColor = MaterialTheme.colors.onSecondary,
                                fillColor = MaterialTheme.colors.error
                            ) { onCancel() }
                            Text(
                                text = stringResource(R.string.tap_to) + "\n" + stringResource(R.string.end_the_call),
                                style = MaterialTheme.typography.h4,
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center
                            )

                        } else {
                            //accept or deny the incoming call
                            IconButtonSmall(
                                resourceId = R.drawable.ic_phone_call,
                                backgroundColor = MaterialTheme.colors.onSecondary,
                                fillColor = MaterialTheme.colors.secondary
                            ) { onAccept() }
                            Text(
                                text = stringResource(R.string.tap_to) + "\n" + stringResource(R.string.accept_the_call),
                                style = MaterialTheme.typography.h4,
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center
                            )
                            IconButtonSmall(
                                resourceId = R.drawable.ic_decline_call,
                                backgroundColor = MaterialTheme.colors.onSecondary,
                                fillColor = MaterialTheme.colors.error
                            ) { onDeny() }
                            Text(
                                text = stringResource(R.string.tap_to) + "\n" + stringResource(R.string.decline_the_call),
                                style = MaterialTheme.typography.h4,
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        IconButtonSmall(
                            resourceId = R.drawable.ic_home_icon,
                            backgroundColor = MaterialTheme.colors.onSecondary,
                            fillColor = MaterialTheme.colors.primary
                        ) { onBack() }
                        Text(
                            text = stringResource(R.string.back_home_description),
                            style = MaterialTheme.typography.h4,
                            color = MaterialTheme.colors.onPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CallFragmentComposablePreview_outgoing() {
    val call = Call(
        randomUUID(),
        callType = CallType.AUDIO,
        callState = CallState.CALLING,
        senderId = randomUUID(),
        receiverId = randomUUID(),
    )
    val callViewState = CallViewState.Calling(call, true)
    AmigoThemeLight {
        CallFragmentComposable("Lukas", "", callViewState, {}, {}, {}, {}) {}
    }
}

@Preview(showBackground = true)
@Composable
fun CallFragmentComposablePreview_incoming() {
    val call = Call(
        randomUUID(),
        callType = CallType.AUDIO,
        callState = CallState.CALLING,
        senderId = randomUUID(),
        receiverId = randomUUID(),
    )
    val callViewState = CallViewState.Calling(call, false)
    AmigoThemeLight {
        CallFragmentComposable("Lukas", "", callViewState, {}, {}, {}, {}) {}
    }
}

@Preview(showBackground = true)
@Composable
fun CallFragmentComposablePreview_started() {
    val call = Call(
        randomUUID(),
        callType = CallType.AUDIO,
        callState = CallState.CANCELLED,
        senderId = randomUUID(),
        receiverId = randomUUID(),
    )
    val callViewState = CallViewState.Accepted(call, false)
    AmigoThemeLight {
        CallFragmentComposable("Lukas", "", callViewState, {}, {}, {}, {}) {}
    }
}