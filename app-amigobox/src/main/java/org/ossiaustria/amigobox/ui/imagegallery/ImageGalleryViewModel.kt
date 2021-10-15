package org.ossiaustria.amigobox.ui.imagegallery

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import org.ossiaustria.amigobox.BoxViewModel
import org.ossiaustria.amigobox.ui.albums.album1
import org.ossiaustria.amigobox.ui.albums.album2
import org.ossiaustria.amigobox.ui.albums.album3
import org.ossiaustria.amigobox.ui.albums.mockUUID1
import org.ossiaustria.amigobox.ui.albums.mockUUID2
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.repositories.AlbumRepository
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class ImageGalleryViewModel(
    ioDispatcher: CoroutineDispatcher,
    private val albumRepository: AlbumRepository
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

    fun getAlbumFromUUIDMock(albumId: UUID): Album {
        if (albumId == mockUUID1) {
            return album1
        } else if (albumId == mockUUID2) {
            return album2
        } else {
            return album3
        }
    }

    fun setNavigationState(navState: GalleryNavState) {
        _navigationState.value = navState
    }

    fun setGalleryIndex(currentGalleryIndex: Int) {
        _currentGalleryIndex.value = currentGalleryIndex
    }

    fun setAutoState(autoState: AutoState) {
        _autoState.value = autoState
    }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    fun getAlbumFromUUID(albumId: UUID) {
        viewModelScope.launch(ioDispatcher) {
            albumRepository.getAlbum(albumId).collect {

                when (it) {

                    is Resource.Success -> _currentAlbum.postValue(it.value)
                    is Resource.Failure -> _currentAlbum
                    else -> Timber.d("$it")//_state.emit(OnboardingState.Unauthenticated)
                }
            }
        }
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

enum class GalleryNavState {
    STOP, PLAY
}

enum class AutoState {
    CHANGE, CHANGED
}

object Utility {

    //time to countdown - 1hr - 60secs
    const val TIME_COUNTDOWN: Long = 5000L
    const val TIME_FORMAT = "%02d:%02d"

}

//convert time to milli seconds
fun Long.formatTime(): String = String.format(
    Utility.TIME_FORMAT,
    TimeUnit.MILLISECONDS.toMinutes(this),
    TimeUnit.MILLISECONDS.toSeconds(this) % 60
)
