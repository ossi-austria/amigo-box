package org.ossiaustria.amigobox.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.UIConstants.HomeButtonRow
import org.ossiaustria.amigobox.ui.UIConstants.ListFragment
import org.ossiaustria.amigobox.ui.UIConstants.NavigationButtonRow
import org.ossiaustria.amigobox.ui.UIConstants.ScrollButton
import org.ossiaustria.amigobox.ui.UIConstants.ScrollableCardList
import org.ossiaustria.amigobox.ui.commons.AmigoThemeLight
import org.ossiaustria.amigobox.ui.commons.NavigationButtonType
import org.ossiaustria.amigobox.ui.commons.ScrollNavigationButton
import org.ossiaustria.amigobox.ui.commons.TextAndIconButton
import org.ossiaustria.lib.domain.models.Album

class AlbumsFragment : Fragment() {

    val navigator: Navigator by inject()

    private val viewModel by viewModel<AlbumsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent { AlbumsScreen(viewModel) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.load()
    }
}

@Composable
fun AlbumsScreen(viewModel: AlbumsViewModel) {
    AmigoThemeLight {
        val albums by viewModel.albums.observeAsState(emptyList())
        Surface(color = MaterialTheme.colors.background) {
            AlbumsFragmentComposable(
                albums,
                viewModel::backToHome,
                viewModel::toAlbum,
            )
        }
    }
}

@Composable
fun AlbumsFragmentComposable(
    albums: List<Album>,
    backToHome: () -> Unit,
    toAlbum: (Album) -> Unit,
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
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
            TextAndIconButton(
                resourceId = R.drawable.ic_home_icon,
                text = stringResource(id = R.string.back_home_description),
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                bottomStart = true,
                topStart = false,
                buttonWidth = UIConstants.BigButtons.BUTTON_WIDTH
            ) {
                backToHome()
            }
            TextAndIconButton(
                resourceId = R.drawable.ic_help_icon,
                text = stringResource(R.string.help_button_description),
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.onPrimary,
                bottomStart = true,
                topStart = false,
                buttonWidth = UIConstants.BigButtons.BUTTON_WIDTH
            ) {
                //TODO: add help screens
            }
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
                text = stringResource(R.string.albums_headline),
                style = MaterialTheme.typography.h3
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
                text = stringResource(R.string.album_usage_description),
                style = MaterialTheme.typography.body1
            )
        }

        // Scrollable List of Albums and Album Descriptions
        val scrollState = rememberScrollState()
        val scope = rememberCoroutineScope()

        Row(
            modifier = Modifier
                .padding(
                    start = ScrollableCardList.PADDING_START,
                    top = ScrollableCardList.PADDING_TOP
                )
                .horizontalScroll(scrollState)

        ) {

            albums.forEach { album ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(ScrollableCardList.CARD_PADDING)
                        .clickable(
                            onClick = { toAlbum(album) }
                        ),
                    elevation = ScrollableCardList.CARD_ELEVATION,
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
                .padding(
                    start = NavigationButtonRow.PADDING_START,
                    end = NavigationButtonRow.PADDING_END,
                    bottom = NavigationButtonRow.PADDING_BOTTOM,
                    top = NavigationButtonRow.PADDING_TOP
                )
                .height(NavigationButtonRow.HEIGHT),
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
                    scrollState.animateScrollTo(scrollState.value - ScrollButton.SCROLL_DISTANCE)
                }
            }

            // Forwards
            ScrollNavigationButton(
                type = NavigationButtonType.NEXT,
                text = stringResource(R.string.next_scroll_navigation_btn),
                scrollState = scrollState
            ) {
                scope.launch {
                    scrollState.animateScrollTo(scrollState.value + ScrollButton.SCROLL_DISTANCE)
                }
            }
        }
    }
}

@Preview
@Composable
fun AlbumsFragmentComposablePreview() {
    MaterialTheme {
        AlbumsFragmentComposable(MOCK_ALBUMS, {}, {})
    }
}