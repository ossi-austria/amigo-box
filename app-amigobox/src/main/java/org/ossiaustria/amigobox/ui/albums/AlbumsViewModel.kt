package org.ossiaustria.amigobox.ui.albums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.repositories.AlbumRepository

class AlbumsViewModel(
    private val albumRepository: AlbumRepository,
    private val navigator: Navigator
) : ViewModel() {

    private fun getAlbums(): List<Album> = mutableListOf(
        album1, album2, album3, album4, album5,
        album6, album7, album8, album9
    )

    fun getThumbnail(album: Album): String = album.items[0].filename

    fun backToHome() {
        navigator.toHome()
    }

    fun toAlbum(album: Album) {
        navigator.toImageGallery(album)
    }

    private val _albums: MutableLiveData<List<Album>> = MutableLiveData()
    val albums: LiveData<List<Album>> = _albums

    fun load() {
        viewModelScope.launch {
            val resource = albumRepository.getAllAlbums().first()
            if (resource.isSuccess && !resource.valueOrNull().isNullOrEmpty()) {
                resource.valueOrNull()?.let {
                    _albums.value = it
                }
            } else {
                _albums.value = getAlbums()
            }
        }
    }
}