package org.ossiaustria.amigobox.ui.imagegallery

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import org.ossiaustria.amigobox.ui.autoplay.TimerNavigationButtonsRow
import org.ossiaustria.amigobox.ui.autoplay.TimerState
import org.ossiaustria.amigobox.ui.commons.HomeButtonsRow
import org.ossiaustria.lib.domain.models.Multimedia

@Composable
fun GalleryFragmentComposable(
    currentIndex: Int,
    timerState: TimerState,
    items: List<Multimedia>,
    toHome: () -> Unit,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onStartStopPressed: () -> Unit,
) {
    ImageBox(currentIndex, items)
    HomeButtonsRow(onClickBack = toHome)

    TimerNavigationButtonsRow(
        timerState = timerState,
        onPreviousPressed = onPreviousPressed,
        onNextPressed = onNextPressed,
        onStartStopPressed = onStartStopPressed,
    )
}

@Preview(
    name = "whole Screen Preview",
    device = Devices.AUTOMOTIVE_1024p,
    widthDp = 720,
    heightDp = 360
)
@Composable
fun PreviewGalleryFragmentComposable() {
    GalleryFragmentComposable(
        currentIndex = 1,
        timerState = TimerState.STOP,
        items = listOf(),
        toHome = {},
        onPreviousPressed = {},
        onNextPressed = {},
        onStartStopPressed = {},
    )
}
