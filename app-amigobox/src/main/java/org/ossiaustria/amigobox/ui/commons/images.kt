package org.ossiaustria.amigobox.ui.commons

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
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
    @SuppressLint("ModifierParameter")
    modifier: Modifier = Modifier.fillMaxSize(),
    @StringRes altTextRes: Int? = null,
    altText: String? = null,
    contentScale: ContentScale = ContentScale.Crop
) {
    val painter = rememberImagePainter(url)

    Box {
        Image(
            painter = painter,
            contentDescription = textResString(altTextRes, altText),
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
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    Image(
        painter = painterResource(R.drawable.ic_launcher_foreground),
        contentDescription = textResString(altText = "Image not found"),
        modifier = modifier.then(AmigoStyle.Images.roundBorders()),
        contentScale = contentScale,
    )
}

@Composable
fun textResString(@StringRes altTextRes: Int? = null, altText: String? = null): String =
    altTextRes?.let { stringResource(altTextRes) } ?: altText ?: ""


