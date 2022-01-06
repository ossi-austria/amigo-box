package org.ossiaustria.amigobox.ui.imagegallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import org.ossiaustria.amigobox.TimedSlideshowViewModel
import org.ossiaustria.lib.domain.models.Album

class ImageGalleryViewModel(
    ioDispatcher: CoroutineDispatcher
) : TimedSlideshowViewModel(ioDispatcher) {

    private val _album: MutableLiveData<Album> = MutableLiveData()
    val album: LiveData<Album> = _album

    fun prepare(album: Album?) {
        if (album != null) {
            slideShowManager.size = album.items.size
            _album.value = album
            startTimer()
        }
    }
}


