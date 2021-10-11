package org.ossiaustria.amigobox.ui.imagegallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import org.ossiaustria.amigobox.BoxViewModel
import org.ossiaustria.lib.domain.models.Album

class ImageGalleryViewModel(
    ioDispatcher: CoroutineDispatcher
) : BoxViewModel(ioDispatcher) {

    private val _currentAlbum = MutableLiveData<Album>()
    val currentAlbum: LiveData<Album> = _currentAlbum
}