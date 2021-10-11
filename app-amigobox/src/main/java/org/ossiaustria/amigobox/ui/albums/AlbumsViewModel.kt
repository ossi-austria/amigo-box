package org.ossiaustria.amigobox.ui.albums

import androidx.lifecycle.ViewModel
import org.ossiaustria.lib.domain.models.Album

class AlbumsViewModel() : ViewModel() {

    fun getAlbums(): List<Album> = mutableListOf(
        album1, album2, album3, album4, album5,
        album6, album7, album8, album9
    )

    fun getThumbnail(album: Album): String = album.items[0].remoteUrl

}