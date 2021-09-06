package org.ossiaustria.amigobox.ui.commons

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.rememberImagePainter
import org.ossiaustria.amigobox.R

/**
 * Automatically downloads/caches an Image via Coil framework
 */
@Composable
fun NetworkImage(
    url: String,
    @StringRes altTextRes: Int? = null,
    altText: String? = null,
    modifier: Modifier,
    contentScale: ContentScale
) {
    val painter = rememberImagePainter(url)

    Box {
        Image(
            painter = painter,
            contentDescription = TextResString(altTextRes, altText),
            modifier = modifier.then(AmigoStyle.Images.roundBorders()),
            contentScale = contentScale,
        )

//        when (painter.loadState) {
//            is ImageLoadState.Loading -> {
//                // Display a circular progress indicator whilst loading
//                CircularProgressIndicator(Modifier.align(Alignment.Center))
//            }
//            is ImageLoadState.Error -> {
//                NotFoundImage(modifier = modifier, contentScale = contentScale)
//            }
//        }
    }
}

@Composable
fun NotFoundImage(
    modifier: Modifier,
    contentScale: ContentScale
) {
    Image(
        painter = painterResource(R.drawable.ic_launcher_foreground),
        contentDescription = TextResString(altText = "Image not found"),
        modifier = modifier.then(AmigoStyle.Images.roundBorders()),
        contentScale = contentScale,
    )
}

@Composable
fun TextResString(@StringRes altTextRes: Int? = null, altText: String? = null): String =
    altTextRes?.let { stringResource(altTextRes) } ?: altText ?: ""


