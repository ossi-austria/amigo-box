package org.ossiaustria.amigobox.ui.albums

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import org.ossiaustria.amigobox.ui.UIConstants.ListCard
import org.ossiaustria.amigobox.ui.UIConstants.ListCardAlbum
import org.ossiaustria.amigobox.ui.commons.NetworkImage
import org.ossiaustria.lib.domain.models.Album

@Composable
fun LoadAlbumCardContent(album: Album, previewImageUrl: String) {
    AlbumCardThumbnail(previewImageUrl)
    AlbumCardTitle(album)
}

@Composable
fun AlbumCardTitle(album: Album) {
    Row(
        modifier = Modifier
            .padding(ListCard.TEXT_PADDING)
            .height(ListCardAlbum.TEXT_HEIGHT)
            .width(ListCard.CARD_WIDTH)
    ) {
        Text(
            text = album.name,
            style = MaterialTheme.typography.h2,
            fontSize = ListCardAlbum.NAME_FONT_SIZE,
            maxLines = 2
        )
    }
}

@Composable
fun AlbumCardThumbnail(previewImageUrl: String) {
    Row(
        modifier = Modifier
            .height(ListCard.AVATAR_IMAGE_HEIGHT)
            .width(ListCard.CARD_WIDTH),
    ) {
        NetworkImage(
            modifier = Modifier.fillMaxSize(),
            url = previewImageUrl,
            contentScale = ContentScale.Crop
        )
    }
}