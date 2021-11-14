package org.ossiaustria.amigobox.ui.autoplay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.NavigationButton
import org.ossiaustria.amigobox.ui.commons.NavigationButtonType
import org.ossiaustria.amigobox.ui.commons.StartAndPauseButton
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.Sendable
import timber.log.Timber
import java.util.concurrent.TimeUnit

// ImageGallery will use this later, but not yet implemented

class AutoplayCommons {
    suspend fun goToImage(
        cancelTimer: () -> Unit,
        listState: LazyListState,
        index: Int?,
        album: Album,
        toAlbums: () -> Unit
    ) {

        if (index != null) {
            if ((index == album.items.size)) {
                cancelTimer()
                toAlbums()
            }
            listState.animateScrollToItem(index)
        }
    }

    suspend fun goToSendable(
        cancelTimer: () -> Unit,
        listState: LazyListState,
        currentIndex: Int?,
        sendables: List<Sendable>,
        toHome: () -> Unit
    ) {
        if (currentIndex != null && sendables.isNotEmpty()) {
            if ((currentIndex == sendables.size)) {
                cancelTimer()
                toHome()
            } else {
                listState.animateScrollToItem(currentIndex)
            }
        }
    }

    private fun playButtonText(
        galleryNavState: GalleryNavState?,
        textStart: String,
        textStop: String
    ): String {
        return when (galleryNavState) {
            GalleryNavState.PLAY -> textStop
            else -> textStart
        }
    }

    private fun nextPressed(
        cancelTimer: () -> Unit,
        setGalleryIndex: (Int) -> Unit,
        startTimer: () -> Unit,
        currentIndex: Int?,
        list: List<Any>,
        navigationState: GalleryNavState?
    ) {
        Timber.w("next pressed!!")
        if (currentIndex != null && currentIndex < list.size) {
            //Timber.w("Ablbum size: " + album.items.size.toString())
            cancelTimer()
            setGalleryIndex(currentIndex + 1)
            if (navigationState == GalleryNavState.PLAY) {
                startTimer()
            }
        }
    }

    private fun startStopPressed(
        startTimer: () -> Unit,
        setNavigationState: (GalleryNavState) -> Unit,
        pauseTimer: () -> Unit,
        galleryNavState: GalleryNavState?
    ) {
        if (galleryNavState == GalleryNavState.STOP) {
            startTimer()
            setNavigationState(GalleryNavState.PLAY)
        } else {
            pauseTimer()
            setNavigationState(GalleryNavState.STOP)
        }
    }

    private fun previousPressed(
        cancelTimer: () -> Unit,
        setGalleryIndex: (Int) -> Unit,
        startTimer: () -> Unit,
        currentIndex: Int?,
        navigationState: GalleryNavState?
    ) {
        if (currentIndex != null && currentIndex > 0) {
            cancelTimer()
            setGalleryIndex(currentIndex - 1)
            if (navigationState == GalleryNavState.PLAY) {
                startTimer()
            }
        }
    }

    // will be used for ImageGallery
    fun handleImages(
        setGalleryIndex: (Int) -> Unit,
        setAutoState: (AutoState) -> Unit,
        time: String,
        currentIndex: Int?,
        autoState: AutoState?
    ) {
        if ((time == "00:00") and (autoState == AutoState.CHANGE)) {
            if (currentIndex != null) {
                setGalleryIndex(currentIndex + 1)
                setAutoState(AutoState.CHANGED)
            }
        } else if ((time != "00:00") and (autoState != AutoState.CHANGE)) {
            setAutoState(AutoState.CHANGE)
        }
    }

    fun handleSendables(
        setGalleryIndex: (Int) -> Unit,
        setAutoState: (AutoState) -> Unit,
        time: String,
        currentIndex: Int?,
        autoState: AutoState?,
    ) {

        if ((time == "00:00") and (autoState == AutoState.CHANGE)) {
            if (currentIndex != null) {
                setGalleryIndex(currentIndex + 1)
                setAutoState(AutoState.CHANGED)
            }
        } else if ((time != "00:00") and (autoState != AutoState.CHANGE)) {
            setAutoState(AutoState.CHANGE)
        }
    }

    @Composable
    fun TimerNavigationButtonsRow(
        cancelTimer: () -> Unit,
        setGalleryIndex: (Int) -> Unit,
        startTimer: () -> Unit,
        currentIndex: Int?,
        navigationState: GalleryNavState?,
        setNavigationState: (GalleryNavState) -> Unit,
        pauseTimer: () -> Unit,
        list: List<Any>,

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
            NavigationButton(
                text = stringResource(R.string.previous_picture_button),
                type = NavigationButtonType.PREVIOUS,
                itemIndex = currentIndex,
                listSize = list.size,
                onClick = {
                    previousPressed(
                        cancelTimer,
                        setGalleryIndex,
                        startTimer,
                        currentIndex,
                        navigationState
                    )
                }

            )
            StartAndPauseButton(
                text = playButtonText(
                    navigationState,
                    stringResource(R.string.start_diashow_button_description),
                    stringResource(R.string.stop_diashow_button_description)
                ),
                state = navigationState,
            ) {
                startStopPressed(
                    startTimer,
                    setNavigationState,
                    pauseTimer,
                    navigationState
                )
            }

            NavigationButton(
                text = stringResource(R.string.next_image_button_description),
                type = NavigationButtonType.NEXT,
                itemIndex = currentIndex,
                listSize = list.size,
                onClick =
                {
                    nextPressed(
                        cancelTimer,
                        setGalleryIndex,
                        startTimer,
                        currentIndex,
                        list,
                        navigationState
                    )
                }
            )
        }
    }

    // usful for testing, not used now
    @Composable
    fun TimerTextRow(time: String) {
        Row(
            modifier = Modifier
                .padding(
                    start = UIConstants.ScrollableCardList.PADDING_START,
                    top = UIConstants.ScrollableCardList.PADDING_TOP
                )
                .fillMaxSize(),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = time
            )
        }
    }
}

enum class GalleryNavState {
    STOP, PLAY
}

enum class AutoState {
    CHANGE, CHANGED
}

object CountdownFormat {
    //time to countdown here 5000 milliseconds = 5 seconds
    const val TIME_COUNTDOWN: Long = 5000L
    const val TIME_FORMAT = "%02d:%02d"
}

//convert time to milli seconds
fun Long.formatTime(): String = String.format(
    CountdownFormat.TIME_FORMAT,
    TimeUnit.MILLISECONDS.toMinutes(this),
    TimeUnit.MILLISECONDS.toSeconds(this) % 60
)
