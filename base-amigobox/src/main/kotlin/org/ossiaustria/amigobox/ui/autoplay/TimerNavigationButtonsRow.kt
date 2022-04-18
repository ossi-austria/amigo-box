package org.ossiaustria.amigobox.ui.autoplay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.HelperButton

@Composable
fun TimerNavigationButtonsRow(
    currentIndex: Int = 0,
    timerState: TimerState,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onStartStopPressed: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(UIConstants.Defaults.INNER_PADDING)
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        HelperButton(
            iconId = R.drawable.ic_arrow_left,
            text = stringResource(R.string.previous_picture_button),
            onClick = onPreviousPressed,
            enabled = currentIndex > 0
        )
        val isPlaying = timerState == TimerState.PLAY
        HelperButton(
            iconId = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
            text = if (isPlaying) {
                stringResource(R.string.stop_diashow_button_description)
            } else stringResource(R.string.start_diashow_button_description),
            onClick = onStartStopPressed
        )
        HelperButton(
            iconId = null,
            endIconId = R.drawable.ic_arrow_right,
            text = stringResource(R.string.next_image_button_description),
            onClick = onNextPressed,
            enabled = timerState != TimerState.FINISHED
        )
    }
}
