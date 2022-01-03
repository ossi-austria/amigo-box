package org.ossiaustria.amigobox.ui.autoplay

import androidx.compose.foundation.lazy.LazyListState
import org.ossiaustria.lib.domain.models.Sendable
import timber.log.Timber
import java.util.concurrent.TimeUnit

class AutoplayCommons {

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

    fun nextPressed(
        cancelTimer: () -> Unit,
        setGalleryIndex: (Int) -> Unit,
        startTimer: () -> Unit,
        currentIndex: Int?,
        size: Int,
        navigationState: GalleryNavState?
    ) {
        Timber.i("next pressed!!")
        if (currentIndex != null && currentIndex < size) {
            cancelTimer()
            setGalleryIndex(currentIndex + 1)
            if (navigationState == GalleryNavState.PLAY) {
                startTimer()
            }
        }
    }

    fun startStopPressed(
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

    fun previousPressed(
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
