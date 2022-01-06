package org.ossiaustria.amigobox.ui.imagegallery

import NotFoundImage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import org.ossiaustria.amigobox.ui.commons.images.NetworkImage
import org.ossiaustria.lib.domain.models.Multimedia

@Composable
fun ImageBox(
    currentIndex: Int,
    items: List<Multimedia>,
) {
    Box {
        val listState = rememberLazyListState()

        LazyRow(
            modifier = Modifier.fillMaxSize(),
            state = listState
        ) {
            items(items = items, itemContent = { item ->
                Box(
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
        }
        LaunchedEffect(currentIndex) {
            listState.animateScrollToItem(currentIndex)
        }
    }
}
