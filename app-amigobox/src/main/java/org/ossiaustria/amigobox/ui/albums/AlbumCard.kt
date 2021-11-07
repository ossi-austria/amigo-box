package org.ossiaustria.amigobox.ui.albums

import NotFoundImage
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants.ListCard
import org.ossiaustria.amigobox.ui.commons.images.NetworkImage
import org.ossiaustria.lib.domain.models.Album

@Composable
fun LoadAlbumCardContent(album: Album, previewImageUrl: String?) {
    AlbumCardThumbnail(previewImageUrl)
    AlbumCardTitle(album)
    Image(
        painterResource(id = R.drawable.ic_rectangle_264),
        modifier = Modifier
            .height(ListCard.RECT_HEIGHT)
            .padding(start = ListCard.TEXT_PADDING),
        contentDescription = null
    )
}

@Composable
fun AlbumCardTitle(album: Album) {
    Row(
        modifier = Modifier
            .padding(
                start = ListCard.TEXT_PADDING,
                end = ListCard.TEXT_PADDING,
                bottom = ListCard.TEXT_PADDING
            )
            .width(ListCard.CARD_WIDTH)
    ) {
        Text(
            text = album.name,
            style = MaterialTheme.typography.h4,
            maxLines = 2
        )
    }
}

@Composable
fun AlbumCardThumbnail(previewImageUrl: String?) {
    Row(
        modifier = Modifier
            .height(ListCard.AVATAR_IMAGE_HEIGHT)
            .width(ListCard.CARD_WIDTH)
    ) {
        if (previewImageUrl.isNullOrBlank()) {
            NotFoundImage()
            // https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50?s=200
        } else {
            NetworkImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ListCard.IMAGE_HEIGHT),
                url = previewImageUrl,
                contentScale = ContentScale.Crop
            )
        }
    }
}