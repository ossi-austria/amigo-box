package org.ossiaustria.amigobox.ui.imagegallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.NetworkImage
import org.ossiaustria.lib.domain.models.Album
import timber.log.Timber

class ImageGalleryFragment : Fragment() {

    val viewModelImage: ImageGalleryViewModel by viewModel<ImageGalleryViewModel>()

    val navigator: Navigator by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val album: Album = Navigator.getAlbum(requireArguments())
            GalleryScreen(album)
        }
    }

    @Composable
    fun GalleryScreen(album: Album) {

        MaterialTheme {
            GalleryFragmentComposable(album)
        }
    }

    @Composable
    fun GalleryFragmentComposable(album: Album) {

        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        LazyRow(
            modifier = Modifier.fillMaxSize(),
            state = listState
        ) {

            items(items = album.items, itemContent = { item ->

                Column(
                    modifier = Modifier.fillParentMaxWidth()
                ) {
                    NetworkImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(onClick = {
                            }),
                        url = item.remoteUrl,
                        contentScale = ContentScale.Fit
                    )
                }
            })

            coroutineScope.launch {

                album.items.forEachIndexed { index, _ ->

                    nextImage(listState, index)
                    Timber.w("Index: %s", index.toString())
                    Timber.w("Size: %s", album.items.size.toString())
                    if (index + 1 == album.items.size) {
                        startAlbums()
                    }
                }
            }
        }
    }

    suspend fun nextImage(listState: LazyListState, index: Int): Boolean {
        listState.animateScrollToItem(index)
        delay(UIConstants.Slideshow.DELAY)
        return true
    }

    private fun startAlbums() {
        navigator.toAlbums()
    }
}

