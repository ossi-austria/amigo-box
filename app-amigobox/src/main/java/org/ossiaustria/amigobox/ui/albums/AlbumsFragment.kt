package org.ossiaustria.amigobox.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.ui.UIConstants.HomeButtonRow
import org.ossiaustria.amigobox.ui.UIConstants.ListFragment
import org.ossiaustria.amigobox.ui.UIConstants.NavigationButtonRow
import org.ossiaustria.amigobox.ui.UIConstants.ScrollButton
import org.ossiaustria.amigobox.ui.UIConstants.ScrollableCardList
import org.ossiaustria.amigobox.ui.commons.MaterialThemeLight
import org.ossiaustria.amigobox.ui.commons.NavigationButton
import org.ossiaustria.amigobox.ui.commons.ScrollButtonType
import org.ossiaustria.amigobox.ui.commons.ScrollNavigationButton
import org.ossiaustria.lib.domain.models.Album

class AlbumsFragment : Fragment() {

    val navigator: Navigator by inject()

    private val viewModel by viewModel<AlbumsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent { AlbumsScreen() }
    }

    @Preview(showBackground = true)
    @Composable
    fun AlbumsScreen() {
        MaterialThemeLight {
            AlbumsFragmentComposable()
        }
    }

    @Composable
    fun AlbumsFragmentComposable() {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        )
        {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = HomeButtonRow.TOP_PADDING,
                        end = HomeButtonRow.END_PADDING
                    )
                    .height(HomeButtonRow.HEIGHT),
                horizontalArrangement = Arrangement.End
            ) {
                // Home Button
                NavigationButton(onClick = { backToHome() }, text = "Zurück zum Start")
            }
            // Header
            Row(
                modifier = Modifier
                    .padding(
                        start = ListFragment.HEADER_PADDING_START,
                        top = ListFragment.HEADER_PADDING_TOP,
                        bottom = ListFragment.HEADER_PADDING_BOTTOM
                    )
                    .height(ListFragment.HEADER_HEIGHT)
            ) {
                Text(
                    text = "Fotos und Alben",
                    fontSize = ListFragment.HEADER_FONT_SIZE
                )
            }

            // Text Description
            Row(
                modifier = Modifier
                    .padding(
                        start = ListFragment.DESCRIPTION_PADDING_START,
                        top = ListFragment.DESCRIPTION_PADDING_TOP
                    )
                    .height(ListFragment.DESCRIPTION_HEIGHT)
            ) {
                Text(
                    text = "Tippe auf ein Bild, um das Album zu öffnen",
                    fontSize = ListFragment.DESCRIPTION_FONT_SIZE
                )
            }

            // Scrollable List of Albums and Album Descriptions

            val scrollState = rememberScrollState()
            // Timber.w("Scrollstate: %s", scrollState.value.toString())
            val scope = rememberCoroutineScope()

            Row(
                modifier = Modifier
                    .padding(
                        start = ScrollableCardList.PADDING_START,
                        top = ScrollableCardList.PADDING_TOP
                    )
                    .horizontalScroll(scrollState)

            ) {

                viewModel.getAlbums().forEach { album ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(ScrollableCardList.CARD_PADDING)
                            .clickable(
                                onClick = { toAlbum(album) }
                            ),
                        elevation = ScrollableCardList.CARD_ELEVATION,
                    )
                    {
                        Column {
                            LoadAlbumCardContent(album, viewModel.getThumbnail(album))
                        }
                    }
                }
            }

            // Back and Forward Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = NavigationButtonRow.PADDING_START,
                        end = NavigationButtonRow.PADDING_END,
                        bottom = NavigationButtonRow.PADDING_BOTTOM
                    )
                    .fillMaxHeight()
                    .height(NavigationButtonRow.HEIGHT),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {

                // Backwards
                ScrollNavigationButton(
                    onClick = {
                        scope.launch {
                            scrollState.animateScrollTo(
                                scrollState.value
                                    - ScrollButton.SCROLL_DISTANCE
                            )
                        }
                    },
                    type = ScrollButtonType.PREVIOUS,
                    text = "Vorherige Seite",
                    scrollState = scrollState,
                )

                // Forwards
                ScrollNavigationButton(
                    onClick = {
                        scope.launch {
                            scrollState.animateScrollTo(
                                scrollState.value
                                    + ScrollButton.SCROLL_DISTANCE
                            )
                        }
                    },
                    type = ScrollButtonType.NEXT,
                    text = "Nächste Seite",
                    scrollState = scrollState
                )
            }
        }
    }

    private fun backToHome() {
        navigator.toHome()
    }

    // using globalState.setCurrentPerson method, to set Person
    private fun toAlbum(album: Album) {
        navigator.toImageGallery(album)
    }
}