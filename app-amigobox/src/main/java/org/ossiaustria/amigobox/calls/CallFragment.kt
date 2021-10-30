package org.ossiaustria.amigobox.calls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.ui.commons.MaterialButton
import org.ossiaustria.amigobox.ui.commons.ProfileImage
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.enums.CallState
import org.ossiaustria.lib.domain.models.enums.CallType
import java.util.UUID.randomUUID

class CallFragment : Fragment() {

    private val callViewModel by viewModel<CallViewModel>()
    private val incomingEventsViewModel by viewModel<IncomingEventsViewModel>()

    val navigator: Navigator by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val call = Navigator.getCall(requireArguments())
        val person = Navigator.getPerson(requireArguments())

        if (call != null) {
            callViewModel.prepareIncomingCall(call)
        } else if (person != null) {
            callViewModel.createNewOutgoingCall(person)
        } else {
            Toast.makeText(context, "Call or Person are both null!", Toast.LENGTH_LONG).show()
        }

        incomingEventsViewModel.startListening()

        callViewModel.state.observe(viewLifecycleOwner) {
            if (it.isActive == true) {
                val token = callViewModel.getToken()
                if (token != null) {
                    navigator.toJitsiCall(it.call.id.toString(), token)
                } else {
                    Toast.makeText(context, "Cannot use Call, no token ? ", Toast.LENGTH_SHORT)
                        .show()
                }
            } else if (it.call.callState == CallState.FINISHED) {
                navigator.back()
            }
        }

        incomingEventsViewModel.notifiedCall.observe(viewLifecycleOwner) {
            callViewModel.onObservedCallChanged(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            MaterialTheme {
                val state by callViewModel.state.observeAsState(null)
                val partner by callViewModel.partner.observeAsState(null)
                if (partner != null && state != null) {
                    CallFragmentComposable(
                        partner!!.name,
                        partner!!.avatarUrl,
                        state!!,
                        onAccept = callViewModel::accept,
                        onCancel = callViewModel::cancel,
                        onDeny = callViewModel::deny,
                    )
                } else {
                    // TODO: Show UI for Loading & Error
                    Text("Loading..")
                }
            }

        }
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
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier) {
                Text(partnerName)
                ProfileImage(partnerAvatarUrl)
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
        ) {
            Column {
                Text(partnerName)
//                Text("callViewState: $callViewState")
                Text(
                    if (callViewState.outgoing) "wird angerufen"
                    else "möchte mit dir reden"
                )
                Text(partnerName)

            }
            if (callViewState.isActive == true) {
                MaterialButton(onClick = onCancel, text = "Finish")
            } else {
                if (callViewState.outgoing) {
                    MaterialButton(onClick = onCancel, text = "Cancel")
                } else {
                    MaterialButton(onClick = onAccept, text = "Accept")
                    MaterialButton(onClick = onDeny, text = "Deny")
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
    MaterialTheme {
        CallFragmentComposable("Lukas", "", callViewState, {}, {}, {})
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
    MaterialTheme {
        CallFragmentComposable("Lukas", "", callViewState, {}, {}, {})
    }
}

@Preview(showBackground = true)
@Composable
fun CallFragmentComposablePreview_started() {
    val call = Call(
        randomUUID(),
        callType = CallType.AUDIO,
        callState = CallState.STARTED,
        senderId = randomUUID(),
        receiverId = randomUUID(),
    )
    val callViewState = CallViewState.Started(call, false)
    MaterialTheme {
        CallFragmentComposable("Lukas", "", callViewState, {}, {}, {})
    }
}