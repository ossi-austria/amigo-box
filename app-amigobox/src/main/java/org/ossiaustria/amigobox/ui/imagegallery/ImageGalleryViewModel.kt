package org.ossiaustria.amigobox.ui.imagegallery

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import org.ossiaustria.amigobox.BoxViewModel
import org.ossiaustria.lib.domain.models.Album

class ImageGalleryViewModel(
    ioDispatcher: CoroutineDispatcher
) : BoxViewModel(ioDispatcher) {

    private val _currentAlbum = MutableLiveData<Album?>()
    val currentAlbum: MutableLiveData<Album?> = _currentAlbum

    private val _navigationState = MutableLiveData(GalleryNavState.PLAY)
    val navigationState: MutableLiveData<GalleryNavState> = _navigationState

    private val _autoState = MutableLiveData(AutoState.CHANGE)
    val autoState: MutableLiveData<AutoState> = _autoState

    private val _currentGalleryIndex = MutableLiveData(0)
    val currentGalleryIndex: MutableLiveData<Int> = _currentGalleryIndex

    // Timer variables
    private var countDownTimer: CountDownTimer? = null

    private val _time = MutableLiveData(Utility.TIME_COUNTDOWN.formatTime())
    val time: LiveData<String> = _time

    // not yet needed but can be used to track progress
    private val _progress = MutableLiveData(1.00F)
    val progress: LiveData<Float> = _progress

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    // set states with following methods
    fun setNavigationState(navState: GalleryNavState) {
        _navigationState.value = navState
    }

    fun setGalleryIndex(currentGalleryIndex: Int) {
        _currentGalleryIndex.value = currentGalleryIndex
    }

    fun setAutoState(autoState: AutoState) {
        _autoState.value = autoState
    }

    fun pauseTimer() {
        countDownTimer?.cancel()
        handleTimerValues(false, Utility.TIME_COUNTDOWN.formatTime(), 1.0F)
    }

    fun startTimer() {

        _isPlaying.value = true
        countDownTimer = object : CountDownTimer(Utility.TIME_COUNTDOWN, 1000) {

            override fun onTick(millisRemaining: Long) {
                val progressValue = millisRemaining.toFloat() / Utility.TIME_COUNTDOWN
                handleTimerValues(true, millisRemaining.formatTime(), progressValue)
            }

            override fun onFinish() {
                startTimer()

            }
        }.start()
    }

    fun cancelTimer() {
        countDownTimer?.cancel()
    }

    private fun handleTimerValues(
        isPlaying: Boolean,
        text: String,
        progress: Float
    ) {
        _isPlaying.value = isPlaying
        _time.value = text
        _progress.value = progress
    }
}


