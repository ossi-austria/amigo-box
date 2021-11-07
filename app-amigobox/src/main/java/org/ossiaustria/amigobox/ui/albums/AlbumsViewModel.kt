package org.ossiaustria.amigobox.ui.albums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.repositories.AlbumRepository

class AlbumsViewModel(
    private val albumRepository: AlbumRepository,
    private val navigator: Navigator
) : ViewModel() {

    fun backToHome() {
        navigator.back()
    }

    fun toAlbum(album: Album) {
        navigator.toImageGallery(album)
    }

    private val _albums: MutableLiveData<List<Album>> = MutableLiveData(emptyList())
    val albums: LiveData<List<Album>> = _albums

    fun load() {
        viewModelScope.launch {
            albumRepository.getAllAlbums().collectLatest { resource ->
                if (resource.isSuccess && !resource.valueOrNull().isNullOrEmpty()) {
                    _albums.value = resource.valueOrNull()
                } else {
                    _albums.value = emptyList()
                }
            }
        }
    }
}