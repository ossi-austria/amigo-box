package org.ossiaustria.amigobox.ui.imagegallery

import NotFoundImage
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.albums.album1
import org.ossiaustria.amigobox.ui.commons.NavigationButton
import org.ossiaustria.amigobox.ui.commons.NavigationButtonType
import org.ossiaustria.amigobox.ui.commons.images.NetworkImage
import org.ossiaustria.lib.domain.models.Multimedia
import timber.log.Timber

class ImageGalleryFragment : Fragment() {

    private val viewModel by viewModel<ImageGalleryViewModel>()

    val navigator: Navigator by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        val album = Navigator.getAlbum(requireArguments())

        setContent {
            if (album != null) {
                GalleryScreen(
                    album.itemsWithMedia,
                    viewModel,
                    ::toAlbums,
                )
            } else {
                Text("Album not provided!")
            }
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

@Composable
fun GalleryScreen(
    items: List<Multimedia>,
    viewModel: ImageGalleryViewModel,
    toAlbums: () -> Unit,
) {

    val navigationState by viewModel.navigationState.observeAsState()
    val currentIndex by viewModel.currentGalleryIndex.observeAsState()
    val time by viewModel.time.observeAsState(CountdownFormat.TIME_COUNTDOWN.formatTime())
    val autoState by viewModel.autoState.observeAsState()

    MaterialTheme {
        GalleryFragmentComposable(
            items,
            toAlbums,
            navigationState,
            currentIndex,
            time,
            autoState,
            viewModel::cancelTimer,
            viewModel::startTimer,
            viewModel::pauseTimer,
            viewModel::setGalleryIndex,
            viewModel::setAutoState,
            viewModel::setNavigationState
        )
    }
}

@Composable
fun GalleryFragmentComposable(
    items: List<Multimedia>,
    toAlbums: () -> Unit,
    navigationState: GalleryNavState?,
    currentIndex: Int?,
    time: String,
    autoState: AutoState?,
    cancelTimer: () -> Unit,
    startTimer: () -> Unit,
    pauseTimer: () -> Unit,
    setGalleryIndex: (Int) -> Unit,
    setAutoState: (AutoState) -> Unit,
    setNavigationState: (GalleryNavState) -> Unit
) {
    ImageBox(
        items,
        cancelTimer,
        currentIndex,
        toAlbums,
        setGalleryIndex,
        setAutoState,
        time,
        autoState
    )
    NavButtonsBox(
        cancelTimer,
        setGalleryIndex,
        startTimer,
        currentIndex,
        navigationState,
        setNavigationState,
        pauseTimer,
        items,
        time
    )
}

@Preview(
    name = "whole Screen Preview",
    device = Devices.AUTOMOTIVE_1024p,
    widthDp = 720,
    heightDp = 360
)
@Composable
fun PreviewGalleryFragmentComposable() {

    GalleryFragmentComposable(
        album1.itemsWithMedia,
        toAlbums = {},
        navigationState = GalleryNavState.STOP,
        currentIndex = 1,
        time = "05:00",
        autoState = AutoState.CHANGED,
        cancelTimer = {},
        startTimer = {},
        pauseTimer = {},
        setGalleryIndex = {},
        setAutoState = {},
        setNavigationState = {}
    )
}

@Composable
fun ImageBox(
    items: List<Multimedia>,
    cancelTimer: () -> Unit,
    currentIndex: Int?,
    toAlbums: () -> Unit,
    setGalleryIndex: (Int) -> Unit,
    setAutoState: (AutoState) -> Unit,
    time: String,
    autoState: AutoState?
) {
    Box {
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        LazyRow(
            modifier = Modifier.fillMaxSize(),
            state = listState
        ) {
            items(items = items, itemContent = { item ->

                Column(
                    modifier = Modifier.fillParentMaxWidth()
                ) {
                    val mediaUrl = item.absoluteMediaUrl()
                    if (mediaUrl != null) {

                        NetworkImage(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable(onClick = {}),

                            url = mediaUrl,
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        NotFoundImage()
                    }
                }
            })
            coroutineScope.launch {
                goToImage(cancelTimer, listState, currentIndex, items, toAlbums)
            }
            handleImages(setGalleryIndex, setAutoState, time, currentIndex, autoState)
        }
    }
}

@Composable
fun NavButtonsBox(
    cancelTimer: () -> Unit,
    setGalleryIndex: (Int) -> Unit,
    startTimer: () -> Unit,
    currentIndex: Int?,
    navigationState: GalleryNavState?,
    setNavigationState: (GalleryNavState) -> Unit,
    pauseTimer: () -> Unit,
    items: List<Multimedia>,
    time: String
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ButtonsRow(
            cancelTimer,
            setGalleryIndex,
            startTimer,
            currentIndex,
            navigationState,
            setNavigationState,
            pauseTimer,
            items,
        )
        TimerTextRow(time)
    }
}

@Composable
fun ButtonsRow(
    cancelTimer: () -> Unit,
    setGalleryIndex: (Int) -> Unit,
    startTimer: () -> Unit,
    currentIndex: Int?,
    navigationState: GalleryNavState?,
    setNavigationState: (GalleryNavState) -> Unit,
    pauseTimer: () -> Unit,
    items: List<Multimedia>,
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
                    cancelTimer,
                    setGalleryIndex,
                    startTimer,
                    currentIndex,
                    navigationState
                )
            },
            text = stringResource(R.string.previous_image_button_description),
            type = NavigationButtonType.PREVIOUS,
            itemIndex = currentIndex,
            listSize = items.size
        )

        /*   StartAndPauseButton(
               text = playButtonText(
                   navigationState,
                   stringResource(R.string.start_diashow_button_description),
                   stringResource(R.string.stop_diashow_button_description)
               ),
               state = navigationState,
               itemIndex = currentIndex,
               listSize = items.size,
           ) {
               startStopPressed(
                   startTimer,
                   setNavigationState,
                   pauseTimer,
                   navigationState
               )
           }

         */
        NavigationButton(
            onClick = {
                nextPressed(
                    cancelTimer,
                    setGalleryIndex,
                    startTimer,
                    currentIndex,
                    items,
                    navigationState
                )
            },
            text = stringResource(R.string.next_image_button_description),
            type = NavigationButtonType.NEXT,
            itemIndex = currentIndex,
            listSize = items.size
        )
    }
}

@Composable
fun TimerTextRow(time: String) {
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

suspend fun goToImage(
    cancelTimer: () -> Unit,
    listState: LazyListState,
    index: Int?,
    items: List<Multimedia>,
    toAlbums: () -> Unit
) {

    if (index != null) {
        if ((index == items.size)) {
            cancelTimer()
            toAlbums()
        }
        listState.animateScrollToItem(index)
    }
}

fun playButtonText(galleryNavState: GalleryNavState?, textStart: String, textStop: String): String {
    when (galleryNavState) {
        GalleryNavState.PLAY -> return textStop
        else -> return textStart
    }
}

fun nextPressed(
    cancelTimer: () -> Unit,
    setGalleryIndex: (Int) -> Unit,
    startTimer: () -> Unit,
    currentIndex: Int?,
    items: List<Multimedia>,
    navigationState: GalleryNavState?
) {
    Timber.w("next pressed!!")
    if (currentIndex != null && currentIndex < items.size) {
        //Timber.w("Ablbum size: " + album.items.size.toString())
        cancelTimer()
        setGalleryIndex(currentIndex + 1)
        if (navigationState == GalleryNavState.PLAY) {
            startTimer()
        }
    }
}

fun startStopPressed(
    startTimer: () -> Unit,
    setNavigationState: (GalleryNavState) -> Unit,
    pauseTimer: () -> Unit,
    galleryNavState: GalleryNavState?
) {
    if (galleryNavState == GalleryNavState.STOP) {
        startTimer()
        setNavigationState(GalleryNavState.PLAY)
    } else {
        pauseTimer()
        setNavigationState(GalleryNavState.STOP)
    }
}

fun previousPressed(
    cancelTimer: () -> Unit,
    setGalleryIndex: (Int) -> Unit,
    startTimer: () -> Unit,
    currentIndex: Int?,
    navigationState: GalleryNavState?
) {
    if (currentIndex != null && currentIndex > 0) {
        cancelTimer()
        setGalleryIndex(currentIndex - 1)
        if (navigationState == GalleryNavState.PLAY) {
            startTimer()
        }
    }
}

fun initTimer(viewModel: ImageGalleryViewModel) {
    viewModel.cancelTimer()
    viewModel.startTimer()
}

fun handleImages(
    setGalleryIndex: (Int) -> Unit,
    setAutoState: (AutoState) -> Unit,
    time: String,
    currentIndex: Int?,
    autoState: AutoState?
) {
    if ((time == "00:00") and (autoState == AutoState.CHANGE)) {
        if (currentIndex != null) {
            setGalleryIndex(currentIndex + 1)
            setAutoState(AutoState.CHANGED)
        }
    } else if ((time != "00:00") and (autoState != AutoState.CHANGE)) {
        setAutoState(AutoState.CHANGE)
    }
}