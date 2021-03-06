package org.ossiaustria.amigobox.ui.albums

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.HomeButtonsRow
import org.ossiaustria.amigobox.ui.commons.NavigationButtonType
import org.ossiaustria.amigobox.ui.commons.ScrollNavigationButton
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.Multimedia
import org.ossiaustria.lib.domain.models.enums.MultimediaType
import java.util.*
import java.util.UUID.randomUUID

@Composable
fun AlbumsFragmentComposable(
    albums: List<Album>,
    backToHome: () -> Unit,
    toAlbum: (Album) -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(
                start = UIConstants.Defaults.OUTER_PADDING,
                top = UIConstants.Defaults.INNER_PADDING,
                end = UIConstants.Defaults.INNER_PADDING
            )
            .verticalScroll(rememberScrollState())
    ) {
        HomeButtonsRow(onClickBack = backToHome)
        Text(
            text = stringResource(R.string.albums_headline),
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .padding(UIConstants.Defaults.INNER_PADDING)

        )

        // Text Description
            Text(
                text = stringResource(R.string.album_usage_description),
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .padding(UIConstants.Defaults.INNER_PADDING)

            )

        // Scrollable List of Albums and Album Descriptions
        val scrollState = rememberScrollState()
        val scope = rememberCoroutineScope()

        Row(
            modifier = Modifier
                .padding(UIConstants.Defaults.TEXT_PADDING)
                .horizontalScroll(scrollState)

        ) {

            albums.forEach { album ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(UIConstants.Defaults.INNER_PADDING)
                        .clickable(onClick = { toAlbum(album) }),
                    elevation = UIConstants.Defaults.CARD_ELEVATION,
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.background(MaterialTheme.colors.background)
                    ) {
                        LoadAlbumCardContent(album, album.thumbnail)
                    }
                }
            }
        }

        // Back and Forward Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(UIConstants.Defaults.INNER_PADDING),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {

            // Backwards
            ScrollNavigationButton(
                type = NavigationButtonType.PREVIOUS,
                text = stringResource(R.string.previous_scroll_navigation_btn),
                scrollState = scrollState,
            ) {
                scope.launch {
                    scrollState.animateScrollTo(scrollState.value - UIConstants.ScrollButton.SCROLL_DISTANCE)
                }
            }

            // Forwards
            ScrollNavigationButton(
                type = NavigationButtonType.NEXT,
                text = stringResource(R.string.next_scroll_navigation_btn),
                scrollState = scrollState
            ) {
                scope.launch {
                    scrollState.animateScrollTo(scrollState.value + UIConstants.ScrollButton.SCROLL_DISTANCE)
                }
            }
        }
    }
}

val multimediaList = 1.until(10).map {
    Multimedia(
        randomUUID(),
        randomUUID(),
        "https://i.picsum.photos/id/238/300/200.jpg?hmac=7sscr7sm3lmziy5VYZR9D3psQi7vltiVEaoux0v0s0M",
        Date(),
        "image/png",
        MultimediaType.IMAGE,
        150,
        randomUUID()
    )
}

@Preview
@Composable
fun AlbumsFragmentComposablePreview() {
    val album1 =
        Album(randomUUID(), "Gartenarbeit 2018", randomUUID(), multimediaList.shuffled().take(5))
    val album2 =
        Album(randomUUID(), "Zweites Album", randomUUID(), multimediaList.shuffled().take(5))
    MaterialTheme {
        AlbumsFragmentComposable(listOf(album1, album2), {}, {})
    }
}