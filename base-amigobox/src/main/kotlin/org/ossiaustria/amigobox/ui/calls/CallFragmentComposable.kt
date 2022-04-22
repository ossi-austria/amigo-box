package org.ossiaustria.amigobox.ui.calls

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.AmigoColors
import org.ossiaustria.amigobox.ui.commons.AmigoThemeLight
import org.ossiaustria.amigobox.ui.commons.DefaultHelpButton
import org.ossiaustria.amigobox.ui.commons.images.ProfileImage
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.enums.CallState
import org.ossiaustria.lib.domain.models.enums.CallType
import org.ossiaustria.lib.jitsi.ui.JitsiWebrtcJsWebView
import java.util.*

@ExperimentalAnimationApi
@Composable
fun CallFragmentComposable(
    jitsiListener: JitsiWebrtcJsWebView.Listener?,
    partnerName: String,
    partnerAvatarUrl: String?,
    callViewState: CallViewState,
    jitsiCommand: JitsiCallComposableCommand?,
    onAccept: () -> Unit,
    onCancel: () -> Unit,
    onDeny: () -> Unit,
    onFinish: () -> Unit,
    onToggleAudio: () -> Unit,
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(AmigoColors.mistyOcean)
    ) {

        val callState = callViewState.call.callState
        JitsiCallComposable(
            jitsiListener,
            callViewState.call.id.toString(),
            callViewState.call.token,
            jitsiCommand
        )

        if (callState != CallState.ACCEPTED) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ProfileImage(
                    partnerAvatarUrl,
                    contentScale = ContentScale.Crop
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (callViewState.outgoing) {
                        //cancel the call
                        BigControlButton(
                            R.drawable.ic_decline_call, R.string.end_the_call, onCancel,
                            color = MaterialTheme.colors.error
                        )
                    } else {
                        //accept or deny the incoming call
                        BigControlButton(
                            R.drawable.ic_phone_call, R.string.accept_the_call, onAccept,
                            color = AmigoColors.mistyOcean
                        )
                        BigControlButton(
                            R.drawable.ic_decline_call, R.string.decline_the_call, onDeny,
                            color = MaterialTheme.colors.error
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, color = MaterialTheme.colors.secondary, shape = RectangleShape)
                    .background(color = MaterialTheme.colors.onSecondary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = partnerName,
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.onPrimary
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(UIConstants.Defaults.INNER_PADDING),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DefaultHelpButton()
                }
            }
            CallBottomControl(
                callViewState,
                onFinish,
                onToggleAudio
            )
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun CallFragmentComposablePreview_outgoing() {
    val call = Call(
        UUID.randomUUID(),
        callType = CallType.AUDIO,
        callState = CallState.CALLING,
        senderId = UUID.randomUUID(),
        receiverId = UUID.randomUUID(),
    )
    val callViewState = CallViewState.Calling(call, true)
    AmigoThemeLight {
        CallFragmentComposable(null, "Lukas", "", callViewState,
            JitsiCallComposableCommand.Prepare, {}, {}, {}, {}) {}
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun CallFragmentComposablePreview_incoming() {
    val call = Call(
        UUID.randomUUID(),
        callType = CallType.AUDIO,
        callState = CallState.CALLING,
        senderId = UUID.randomUUID(),
        receiverId = UUID.randomUUID(),
    )
    val callViewState = CallViewState.Calling(call, false)
    AmigoThemeLight {
        CallFragmentComposable(
            null, "Lukas",
            "",
            callViewState,
            JitsiCallComposableCommand.Prepare,
            {}, {}, {}, {}) {}
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun CallFragmentComposablePreview_started() {
    val call = Call(
        UUID.randomUUID(),
        callType = CallType.AUDIO,
        callState = CallState.CANCELLED,
        senderId = UUID.randomUUID(),
        receiverId = UUID.randomUUID(),
    )
    val callViewState = CallViewState.Accepted(call, false, false)
    AmigoThemeLight {
        CallFragmentComposable(
            null, "Lukas",
            "",
            callViewState,
            JitsiCallComposableCommand.Prepare,
            {}, {}, {}, {}) {}
    }
}