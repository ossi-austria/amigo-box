package org.ossiaustria.amigobox.ui.imagegallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.NavigationButton
import org.ossiaustria.amigobox.ui.commons.NetworkImage
import org.ossiaustria.lib.domain.models.Album
import timber.log.Timber

class ImageGalleryFragment : Fragment() {

    private val viewModel by viewModel<ImageGalleryViewModel>()
    val imageGalleryHelper: ImageGalleryHelper = ImageGalleryHelper()

    val navigator: Navigator by inject()

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        val album = Navigator.getAlbum(requireArguments())
        setContent {
            GalleryScreen(
                album,
                viewModel,
                ::toAlbums
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init stuff
        viewModel.setGalleryIndex(0)
        initTimer(viewModel)
    }

    fun toAlbums() {
        navigator.toAlbums()
    }

}

@InternalCoroutinesApi
@Composable
fun GalleryScreen(
    album: Album,
    viewModel: ImageGalleryViewModel,
    toAlbums: () -> Unit,
) {

    if (album != null) {

        MaterialTheme {
            GalleryFragmentComposable(
                album,
                viewModel,
                toAlbums
            )

        }

    } else Text("Album cannot be loaded")
}

@Composable
fun GalleryFragmentComposable(
    album: Album,
    viewModel: ImageGalleryViewModel,
    toAlbums: () -> Unit
) {

    val navigationState by viewModel.navigationState.observeAsState()
    val currentIndex by viewModel.currentGalleryIndex.observeAsState()
    val time by viewModel.time.observeAsState(Utility.TIME_COUNTDOWN.formatTime())
    val autoState by viewModel.autoState.observeAsState()

    Timber.w("time: $time")

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Box {
        LazyRow(
            modifier = Modifier.fillMaxSize(),
        ) {
            items(items = album.items, itemContent = { _ ->

                coroutineScope.launch {
                    goToImage(viewModel, listState, currentIndex, album, toAlbums)
                }

                handleImages(viewModel, time, currentIndex, autoState)
                Column(
                    modifier = Modifier.fillParentMaxWidth()
                ) {
                    NetworkImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(onClick = {
                            }),
                        // TODO: needs some clarification - this is just for mocking, not sure where to get url and file from
                        url = "https://i.picsum.photos/id/238/300/200.jpg?hmac=7sscr7sm3lmziy5VYZR9D3psQi7vltiVEaoux0v0s0M",
                        contentScale = ContentScale.Fit
                    )
                }
            })

        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = UIConstants.ScrollableCardList.PADDING_START,
                    top = UIConstants.ScrollableCardList.PADDING_TOP
                )
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            NavigationButton(
                onClick = {
                    previousPressed(
                        viewModel,
                        currentIndex,
                        navigationState
                    )
                },
                text = "Vorheriges Bild"
            )
            NavigationButton(
                onClick = { startStopPressed(viewModel, navigationState) },
                text = playButtonText(navigationState)
            )
            NavigationButton(
                onClick = {
                    nextPressed(
                        viewModel,
                        currentIndex,
                        album,
                        navigationState
                    )
                },
                text = "Nächstes Bild"
            )
        }
        Row(
            modifier = Modifier
                .padding(
                    start = UIConstants.ScrollableCardList.PADDING_START,
                    top = UIConstants.ScrollableCardList.PADDING_TOP
                )
                .fillMaxSize(),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = time
            )
        }
    }
}

suspend fun goToImage(
    viewModel: ImageGalleryViewModel,
    listState: LazyListState,
    index: Int?,
    album: Album,
    toAlbums: () -> Unit
) {

    if (index != null) {
        if ((index == album.items.size)) {
            viewModel.cancelTimer()
            toAlbums()
        }
        Timber.w("visible index: " + listState.firstVisibleItemIndex)
        listState.animateScrollToItem(index)
    }
}

fun playButtonText(galleryNavState: GalleryNavState?): String {
    when (galleryNavState) {
        GalleryNavState.PLAY -> return "Diashow anhalten"
        else -> return "Diashow starten"
    }
}

fun nextPressed(
    viewModel: ImageGalleryViewModel,
    currentIndex: Int?,
    album: Album,
    navigationState: GalleryNavState?
) {
    Timber.w("next pressed!!")
    if (currentIndex != null && currentIndex < album.items.size) {
        Timber.w("Ablbum size: " + album.items.size.toString())
        viewModel.cancelTimer()
        viewModel.setGalleryIndex(currentIndex + 1)
        if (navigationState == GalleryNavState.PLAY) {
            viewModel.startTimer()
        }
    }

}

fun startStopPressed(viewModel: ImageGalleryViewModel, galleryNavState: GalleryNavState?) {
    if (galleryNavState == GalleryNavState.STOP) {
        viewModel.startTimer()
        viewModel.setNavigationState(GalleryNavState.PLAY)
    } else {
        viewModel.pauseTimer()
        viewModel.setNavigationState(GalleryNavState.STOP)
    }
}

fun previousPressed(
    viewModel: ImageGalleryViewModel,
    currentIndex: Int?,
    navigationState: GalleryNavState?
) {
    if (currentIndex != null && currentIndex > 0) {
        viewModel.cancelTimer()
        viewModel.setGalleryIndex(currentIndex - 1)
        if (navigationState == GalleryNavState.PLAY) {
            viewModel.startTimer()
        }
    }
}

fun initTimer(viewModel: ImageGalleryViewModel) {
    viewModel.cancelTimer()
    viewModel.startTimer()
}

fun handleImages(
    viewModel: ImageGalleryViewModel,
    time: String,
    currentIndex: Int?,
    autoState: AutoState?
) {
    Timber.w("setting Index")
    Timber.w("Current Index: " + currentIndex.toString())
    if ((time == "00:00") and (autoState == AutoState.CHANGE)) {
        if (currentIndex != null) {
            viewModel.setGalleryIndex(currentIndex + 1)
            viewModel.setAutoState(AutoState.CHANGED)
        }
    } else if ((time != "00:00") and (autoState != AutoState.CHANGE)) {
        viewModel.setAutoState(AutoState.CHANGE)
    }
}