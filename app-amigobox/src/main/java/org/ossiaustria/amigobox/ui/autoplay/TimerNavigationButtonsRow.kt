package org.ossiaustria.amigobox.ui.autoplay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.TextAndIconButton

@Composable
fun TimerNavigationButtonsRow(
    cancelTimer: () -> Unit,
    setGalleryIndex: (Int) -> Unit,
    startTimer: () -> Unit,
    currentIndex: Int?,
    navigationState: GalleryNavState?,
    setNavigationState: (GalleryNavState) -> Unit,
    pauseTimer: () -> Unit,
    size: Int,
    autoplayCommons: AutoplayCommons
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = UIConstants.NavigationButtonRow.PADDING_START,
                end = UIConstants.NavigationButtonRow.PADDING_END,
                bottom = UIConstants.NavigationButtonRow.PADDING_BOTTOM
            )
            .fillMaxHeight()
            .height(UIConstants.NavigationButtonRow.HEIGHT),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        TextAndIconButton(
            iconId = R.drawable.ic_arrow_left,
            text = stringResource(R.string.previous_picture_button),
            onClick = {
                autoplayCommons.previousPressed(
                    cancelTimer,
                    setGalleryIndex,
                    startTimer,
                    currentIndex,
                    navigationState
                )
            }
        )
        val isPlaying = navigationState == GalleryNavState.PLAY
        TextAndIconButton(
            bottomStart = false,
            iconId = if (isPlaying) R.drawable.ic_play else R.drawable.ic_pause,
            text = if (isPlaying) {
                stringResource(R.string.stop_diashow_button_description)
            } else stringResource(R.string.start_diashow_button_description),
            onClick = {
                autoplayCommons.startStopPressed(
                    startTimer,
                    setNavigationState,
                    pauseTimer,
                    navigationState
                )
            }
        )
        TextAndIconButton(
            iconId = null,
            endIconId = R.drawable.ic_arrow_right,
            text = stringResource(R.string.next_image_button_description),
            onClick = {
                autoplayCommons.nextPressed(
                    cancelTimer,
                    setGalleryIndex,
                    startTimer,
                    currentIndex,
                    size,
                    navigationState
                )
            }
        )
    }
}
