package org.ossiaustria.amigobox.ui.calls

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import org.ossiaustria.amigobox.ui.commons.PreviewTheme
import org.ossiaustria.lib.jitsi.ui.JitsiWebrtcJsWebView
import timber.log.Timber

sealed class JitsiCallComposableCommand {
    object Prepare : JitsiCallComposableCommand()
    object EnterRoom : JitsiCallComposableCommand()
    object Finish : JitsiCallComposableCommand()
    object ToggleAudio : JitsiCallComposableCommand()
    object Null : JitsiCallComposableCommand()
}

@Composable
fun JitsiCallComposable(
    listener: JitsiWebrtcJsWebView.Listener?,
    room: String?,
    token: String?,
    command: JitsiCallComposableCommand? = null
) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            // Creates custom view
            JitsiWebrtcJsWebView(context, null).apply {
                prepare(listener)
                onConnectionFailedListener = { Timber.e("FAILED") }
                // enter "empty" room, just for init
                enterRoom("amigo-dev.ossi-austria.org", "", "")
            }
        },
        update = { jitsi ->
            if (command != null) {

                jitsi.triggerStatusRefresh()

                Timber.i("Run command $command")
                when (command) {
                    JitsiCallComposableCommand.Prepare -> jitsi.keepHidden()
                    JitsiCallComposableCommand.EnterRoom -> jitsi.enterRoom(
                        "amigo-dev.ossi-austria.org",
                        room,
                        token
                    )
                    JitsiCallComposableCommand.Finish -> jitsi.finish()
                    JitsiCallComposableCommand.ToggleAudio -> jitsi.toggleAudio()
                }
            }
            jitsi.triggerStatusRefresh()
        }
    )
}

@Composable
@Preview
fun JitsiCallComposableExample() {
    PreviewTheme {
        JitsiCallComposable(null, "room", "token")
    }
}