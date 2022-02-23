package org.ossiaustria.amigobox

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineDispatcher
import org.ossiaustria.amigobox.ui.autoplay.HasSlideShowManager
import org.ossiaustria.amigobox.ui.autoplay.TimedSlideShowManager
import org.ossiaustria.amigobox.ui.autoplay.TimerState

abstract class TimedSlideshowViewModel(
    ioDispatcher: CoroutineDispatcher
) : BoxViewModel(ioDispatcher), HasSlideShowManager {

    protected val slideShowManager: TimedSlideShowManager = TimedSlideShowManager()

    val timerState: LiveData<TimerState> = slideShowManager.timerState
    val currentIndex: LiveData<Int> = slideShowManager.currentIndex

    override fun onPreviousPressed() {
        slideShowManager.decrementIndex()
    }

    override fun onNextPressed() {
        slideShowManager.incrementIndex()
    }

    override fun onStartStopPressed() {
        slideShowManager.startStopPressed()
    }

    fun startTimer() {
        slideShowManager.startTimer()
    }

    fun onPause() {
        slideShowManager.stopTimer()
    }
}