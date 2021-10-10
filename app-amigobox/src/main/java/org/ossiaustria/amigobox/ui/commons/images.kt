package org.ossiaustria.amigobox.ui.commons

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.rememberImagePainter
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants

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

// I was unable to create the exact image view as in Figma, so I decided to use circle view.
// For the future use ProfilImageShape and try different approach
@Composable
fun profileImage(
    url: String,
    @SuppressLint("ModifierParameter")
    modifier: Modifier = Modifier.fillMaxSize(),
    @StringRes altTextRes: Int? = null,
    altText: String? = null,
    contentScale: ContentScale = ContentScale.FillBounds,

    ) {
    val painter = rememberImagePainter(url)
    Image(
        painter = painter,
        contentDescription = textResString(altTextRes, altText),
        contentScale = contentScale,
        modifier = Modifier
            .size(UIConstants.ProfileImage.IMAGE_SIZE)
            .padding(UIConstants.ProfileImage.IMAGE_PADDING)
            .clip(CircleShape) //.clip(ProfilImageShape)
            .border(
                UIConstants.ProfileImage.BORDER_WIDTH,
                MaterialTheme.colors.surface,
                CircleShape
            )
    )

}

