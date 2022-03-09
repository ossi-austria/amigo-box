package org.ossiaustria.amigobox.ui.timeline.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.PreviewTheme
import org.ossiaustria.amigobox.ui.commons.TextAndIconButton
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.AlbumShare
import org.ossiaustria.lib.domain.models.Multimedia
import org.ossiaustria.lib.domain.models.enums.MultimediaType
import java.util.UUID.randomUUID

@Composable
fun AlbumShareContent(
    albumShare: AlbumShare,
    toAlbum: (Album) -> Unit
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        //TODO: Show some Album items thumbnails
        Text(
            text = albumShare.album.name,
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier.padding(bottom = UIConstants.TimelineFragment.PADDING)
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
}

@Composable
@Preview
fun AlbumShareContentPreview() {
    PreviewTheme {
        val albumId = randomUUID()
        val album = Album(
            id = albumId,
            name = "Album",
            items = listOf(
                Multimedia(
                    randomUUID(),
                    ContentMocks.otherPerson.id,
                    filename = "Filename",
                    contentType = "JPG",
                    type = MultimediaType.VIDEO,
                    albumId = albumId
                )
            ),
            ownerId = ContentMocks.otherPerson.id,
        )

        val albumShare = AlbumShare(
            randomUUID(),
            senderId = ContentMocks.otherPerson.id,
            receiverId = ContentMocks.centerPerson.id,
            album = album
        )
        AlbumShareContent(albumShare, {})
    }
}