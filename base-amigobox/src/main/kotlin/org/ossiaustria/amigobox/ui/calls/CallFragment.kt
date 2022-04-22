package org.ossiaustria.amigobox.ui.calls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.commons.AmigoThemeLight
import org.ossiaustria.amigobox.ui.commons.Toasts
import org.ossiaustria.lib.domain.services.events.IncomingEventCallbackService
import timber.log.Timber

class CallFragment : Fragment() {

    private val callViewModel by viewModel<CallViewModel>()
    private val incomingEventCallbackService: IncomingEventCallbackService by inject()

    private val scope = CoroutineScope(get<CoroutineDispatcher>())

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
//            val call1 = callViewModel.state.value?.call
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
                    //                navigator.toJitsiCall(it.call.id.toString(), token)
                } else {
                    Toasts.showLong(requireContext(), "Cannot use Call, no token ? ")
                }
            } else {
                phoneSoundManager.stopAll()
                val textRes = when (it) {
                    is CallViewState.Finished -> R.string.call_finished
                    is CallViewState.Cancelled -> R.string.call_cancelled
                    is CallViewState.Failure -> R.string.call_failure
                    is CallViewState.Timeout -> R.string.call_timeout
                    else -> R.string.call_cancelled
                }
                Toasts.showLong(requireContext(), requireContext().getString(textRes))
                scope.launch {
                    navigator.autoBack()
                }
            }
        }

        /**
         * Observe the FCM via "incomingEventsViewModel", needed for both INCOMING & OUTGOING
         * Could happen at any time, but usually will happen when the other party performs an operation
         * like: ACCEPT, CANCEL, DENY, FINISH
         *
         * This will use CallViewModel and trigger a state change, see callViewModel.state
         */
        incomingEventCallbackService.callEventFlow
            .onEach { callViewModel.onObservedCallChanged(it) }
            .onCompletion { cause -> if (cause == null) Timber.i("Flow is completed successfully") }
            .catch { cause -> Timber.w("Exception $cause happened") }
            .launchIn(scope)

        scope.launch {
            phoneSoundManager.prepare(requireContext())
        }
    }

    @OptIn(ExperimentalAnimationApi::class, ExperimentalAnimationApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {

            AmigoThemeLight {
                val state by callViewModel.state.observeAsState(null)
                val partner by callViewModel.partner.observeAsState(null)
                val jitsiListener = callViewModel.jitsiListener
                val jitsiCommand by callViewModel.jitsiCommand.observeAsState(null)
                if (partner != null && state != null) {
                    Surface(color = MaterialTheme.colors.secondary) {
                        CallFragmentComposable(
                            jitsiListener,
                            partner!!.name,
                            partner!!.absoluteAvatarUrl(),
                            state!!,
                            jitsiCommand,
                            onAccept = callViewModel::accept,
                            onCancel = callViewModel::cancel,
                            onDeny = callViewModel::deny,
                            onFinish = callViewModel::finish,
                            onToggleAudio = callViewModel::onToggleAudio,
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            state != null && partner == null -> Text(stringResource(id = R.string.person_not_available))
                            state is CallViewState.Failure -> Text(text = (state as CallViewState.Failure).error.toString())
                            else -> Text(stringResource(id = R.string.call_fragment_loading))
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
        scope.cancel()
    }
}

