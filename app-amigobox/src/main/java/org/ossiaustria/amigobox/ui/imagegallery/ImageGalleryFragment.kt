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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.contacts.GlobalStateViewModel
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.NetworkImage
import org.ossiaustria.lib.domain.models.Album
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ImageGalleryFragment : Fragment() {

    private val globalState: GlobalStateViewModel by activityViewModels()
    private val viewModelImage: ImageGalleryViewModel by activityViewModels()

    @Inject
    lateinit var navigator: Navigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {

//            val scope = rememberCoroutineScope()
//            scope.launch{ viewModelImage.autoImageGallery() }
            val album: Album = globalState.selectedAlbum.value!!

            GalleryScreen(album)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            modifier = Modifier
                .fillMaxSize(),
            state = listState


        ) {

            items(items = album.items, itemContent = { item ->

                Column(
                    modifier = Modifier
                        .fillParentMaxWidth()
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

                album.items.forEachIndexed() { index, _ ->

                    nextImage(listState, index)
                    Timber.w("Index: %s", index.toString())
                    Timber.w("Size: %s", album.items.size.toString())
                    if (index+1 == album.items.size){
                        startAlbums()
                    }

            } }


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

