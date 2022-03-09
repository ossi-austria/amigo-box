package org.ossiaustria.amigobox.ui.commons.images


import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.textResString

// I was unable to create the exact image view as in Figma, so I decided to use circle view.
// For the future use ProfilImageShape and try different approach
@ExperimentalCoilApi
@Composable
fun ProfileImage(
    url: String?,
    @StringRes altTextRes: Int? = null,
    altText: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    onClick: () -> Unit = {}
) {
    val painter = if (!url.isNullOrBlank()) {
        rememberImagePainter(url)
    } else {
        painterResource(R.drawable.ic_image_light)
    }
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
            .clickable(onClick = onClick)
    )
}

