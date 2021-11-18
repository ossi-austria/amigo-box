package org.ossiaustria.amigobox.ui.imagegallery

import NotFoundImage
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
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
import org.ossiaustria.amigobox.ui.autoplay.AutoplayCommons
import org.ossiaustria.amigobox.ui.autoplay.GalleryNavState
import org.ossiaustria.amigobox.ui.commons.AmigoThemeLight
import org.ossiaustria.amigobox.ui.commons.IconButtonSmall
import org.ossiaustria.amigobox.ui.commons.images.NetworkImage
import org.ossiaustria.lib.domain.models.Multimedia

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
                AmigoThemeLight {
                    Surface(color = MaterialTheme.colors.secondary) {
                        GalleryScreen(
                                album.itemsWithMedia,
                                viewModel,
                                ::toAlbums,
                                ::toHome,
                        )
                    }
                }
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

    fun toHome() {
        navigator.back()
    }

}

@Composable
fun GalleryScreen(
        items: List<Multimedia>,
        viewModel: ImageGalleryViewModel,
        toAlbums: () -> Unit,
        toHome: () -> Unit,
) {
    val autoplay = AutoplayCommons()

    val navigationState by viewModel.navigationState.observeAsState()
    val currentIndex by viewModel.currentGalleryIndex.observeAsState()
    val time by viewModel.time.observeAsState(CountdownFormat.TIME_COUNTDOWN.formatTime())
    val autoState by viewModel.autoState.observeAsState()


    GalleryFragmentComposable(
            items,
            toAlbums,
            toHome,
            navigationState,
            currentIndex,
            time,
            autoState,
            viewModel::cancelTimer,
            viewModel::startTimer,
            viewModel::pauseTimer,
            viewModel::setGalleryIndex,
            viewModel::setAutoState,
            viewModel::setNavigationState,
            autoplay
    )
}

@Composable
fun GalleryFragmentComposable(
        items: List<Multimedia>,
        toAlbums: () -> Unit,
        toHome: () -> Unit,
        navigationState: GalleryNavState?,
        currentIndex: Int?,
        time: String,
        autoState: AutoState?,
        cancelTimer: () -> Unit,
        startTimer: () -> Unit,
        pauseTimer: () -> Unit,
        setGalleryIndex: (Int) -> Unit,
        setAutoState: (AutoState) -> Unit,
        setNavigationState: (GalleryNavState) -> Unit,
        autoplay: AutoplayCommons
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
    HomeAndHelpRow(
            toHome
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
            time,
            autoplay
    )
}

@Composable
fun HomeAndHelpRow(toHome: () -> Unit) {
    Box(modifier = Modifier
            .fillMaxSize())
    {
        Row(
                Modifier
                        .padding(
                                top = UIConstants.HomeButtonRow.TOP_PADDING,
                                end = UIConstants.HomeButtonRow.END_PADDING
                        )
                        .fillMaxWidth()
                        .height(UIConstants.HomeButtonRow.HEIGHT),
                horizontalArrangement = Arrangement.End
        ) {
            IconButtonSmall(
                    resourceId = R.drawable.ic_home_icon,
                    backgroundColor = MaterialTheme.colors.secondary,
                    fillColor = MaterialTheme.colors.surface,
            ) {
                toHome()
            }
            IconButtonSmall(
                    resourceId = R.drawable.ic_help_icon,
                    backgroundColor = MaterialTheme.colors.secondary,
                    fillColor = MaterialTheme.colors.primary,
            ) {
                //TODO: Add help screens
            }
        }
    }
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
            toHome = {},
            navigationState = GalleryNavState.STOP,
            currentIndex = 1,
            time = "05:00",
            autoState = AutoState.CHANGED,
            cancelTimer = {},
            startTimer = {},
            pauseTimer = {},
            setGalleryIndex = {},
            setAutoState = {},
            setNavigationState = {},
            autoplay = AutoplayCommons()
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
        time: String,
        autoplay: AutoplayCommons,

        ) {
    Box(
            modifier = Modifier.fillMaxSize()
    ) {

        autoplay.TimerNavigationButtonsRow(
                cancelTimer,
                setGalleryIndex,
                startTimer,
                currentIndex,
                navigationState,
                setNavigationState,
                pauseTimer,
                items
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