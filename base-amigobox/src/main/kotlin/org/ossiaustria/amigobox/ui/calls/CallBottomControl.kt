package org.ossiaustria.amigobox.ui.calls

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.TextAndIconButton

@Composable
fun CallBottomControl(
    callViewState: CallViewState,
    onAccept: () -> Unit,
    onCancel: () -> Unit,
    onDeny: () -> Unit,
    onFinish: () -> Unit,
    onToggleAudio: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, color = MaterialTheme.colors.secondary, shape = RectangleShape)
            .background(color = MaterialTheme.colors.onSecondary),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = UIConstants.Defaults.OUTER_PADDING,
                    vertical = UIConstants.Defaults.INNER_PADDING
                )
        ) {

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
                    callViewState.error?.toString() ?: stringResource(R.string.call_failure)
                } else {
                    ""
                },

                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.padding(vertical = UIConstants.Defaults.INNER_PADDING)
            )
        }

        Row(
            modifier = Modifier
                .padding(
                    horizontal = UIConstants.Defaults.OUTER_PADDING,
                    vertical = UIConstants.Defaults.INNER_PADDING
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (callViewState is CallViewState.Accepted) {
                if (callViewState.isMuted) {
                    ControlButton(
                        R.drawable.ic_microphone_off,
                        R.string.set_microphone_on,
                        onToggleAudio
                    )
                } else {
                    ControlButton(
                        R.drawable.ic_microphone,
                        R.string.set_microphone_off,
                        onToggleAudio
                    )
                }
                ControlButton(
                    R.drawable.ic_decline_call,
                    R.string.end_the_call,
                    onFinish,
                    color = MaterialTheme.colors.error
                )
            }
        }
    }

}

@Composable
fun ControlButton(
    @DrawableRes iconResId: Int,
    @StringRes textResId: Int,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colors.primary
) {
    TextAndIconButton(
        iconId = iconResId,
        text = stringResource(textResId),
        backgroundColor = color,
        onClick = onClick
    )
}

@Composable
fun BigControlButton(
    @DrawableRes iconResId: Int,
    @StringRes textResId: Int,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colors.primary
) {
    TextAndIconButton(
        iconId = iconResId,
        text = stringResource(textResId),
        backgroundColor = color,
        onClick = onClick,
        padding = 40.dp
    )
}