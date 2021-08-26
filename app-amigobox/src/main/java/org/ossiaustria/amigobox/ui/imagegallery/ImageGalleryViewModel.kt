package org.ossiaustria.amigobox.ui.imagegallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import org.ossiaustria.amigobox.BoxViewModel
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.Multimedia
import timber.log.Timber
import java.sql.Time
import javax.inject.Inject

@HiltViewModel
class ImageGalleryViewModel @Inject constructor(
    ioDispatcher: CoroutineDispatcher
) : BoxViewModel(ioDispatcher){

    private val _currentAlbum = MutableLiveData<Album>()
    val currentAlbum: LiveData<Album> = _currentAlbum
}