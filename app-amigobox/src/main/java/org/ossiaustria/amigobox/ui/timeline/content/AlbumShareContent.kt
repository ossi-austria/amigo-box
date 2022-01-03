package org.ossiaustria.amigobox.ui.timeline.content

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.PreviewTheme
import org.ossiaustria.amigobox.ui.commons.TextAndIconButton
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.AlbumShare

@Composable
fun AlbumShareContent(
    albumShare: AlbumShare,
    toAlbum: (Album) -> Unit
) {

    //TODO: Show some Album items thumbnails
    Text(
        text = albumShare.album.name,
        style = MaterialTheme.typography.h1,
        color = MaterialTheme.colors.onSecondary,
        modifier = Modifier.padding(bottom = UIConstants.TimelineFragment.BOTTOM_PADDING)
    )
    TextAndIconButton(
        iconId = R.drawable.ic_image_light,
        text = stringResource(R.string.open_album_button),
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        bottomStart = false,
        topStart = true,
    ) {
        if (albumShare.album.items.isNotEmpty()) {
            toAlbum(albumShare.album)
        }
    }
}

@Composable
@Preview
fun AlbumShareContentPreview() {
    PreviewTheme {
        AlbumShareContent(ContentMocks.albumShare, {})
    }
}