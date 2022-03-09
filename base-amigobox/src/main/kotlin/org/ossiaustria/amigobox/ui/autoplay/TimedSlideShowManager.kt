package org.ossiaustria.amigobox.ui.autoplay

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.math.max
import kotlin.math.min

enum class TimerState {
    STOP, PLAY, FINISHED
}

class TimedSlideShowManager {

    private val _timerState = MutableLiveData(TimerState.PLAY)
    val timerState: MutableLiveData<TimerState> = _timerState

    private val _currentIndex = MutableLiveData(0)
    val currentIndex: MutableLiveData<Int> = _currentIndex

    private val _currentCountdown = MutableLiveData(TIME_COUNTDOWN_MS)
    val currentCountdown: LiveData<Long> = _currentCountdown

    var size: Int = 0

    private var countDownTimer: CountDownTimer? = null

    fun startTimer() {
        _timerState.value = TimerState.PLAY
        internalStartTimer()
    }

    private fun internalStartTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(TIME_COUNTDOWN_MS, 1000) {

            override fun onTick(millisRemaining: Long) {
                handleTimerValues(millisRemaining)
            }

            override fun onFinish() {
                incrementIndex()
                if (_timerState.value == TimerState.PLAY) {
                    internalStartTimer()
                }
            }
        }.start()
    }

    fun stopTimer() {
        _timerState.value = TimerState.STOP

        countDownTimer?.cancel()
        countDownTimer = null
    }

    fun startStopPressed() {
        if (_timerState.value == TimerState.STOP) {
            startTimer()
        } else {
            stopTimer()
        }
    }

    fun decrementIndex() {
        setGalleryIndex(max((_currentIndex.value ?: 0) - 1, 0))
    }

    fun incrementIndex() {
        if (_currentIndex.value ?: 0 >= size - 1) {
            _timerState.value = TimerState.FINISHED
        }
        setGalleryIndex(min((_currentIndex.value ?: 0) + 1, size - 1))
    }

    private fun setGalleryIndex(currentGalleryIndex: Int) {
        _currentIndex.value = currentGalleryIndex
    }

    private fun handleTimerValues(countdown: Long) {
        _currentCountdown.value = countdown
    }

    companion object {
        const val TIME_COUNTDOWN_MS: Long = 15000L
    }
}