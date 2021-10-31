package org.ossiaustria.amigobox.ui.commons.images

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberImagePainter
import org.ossiaustria.amigobox.ui.commons.AmigoStyle
import org.ossiaustria.amigobox.ui.commons.AmigoThemeLight
import org.ossiaustria.amigobox.ui.commons.textResString

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
    val painter = rememberImagePainter(
        data = url,
        builder = {
            crossfade(true)
        }
    )
    Box {
        Image(
            painter = painter,
            contentDescription = textResString(altTextRes, altText),
            modifier = modifier.then(AmigoStyle.Images.roundBorders()),
            contentScale = contentScale,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun NetworkImagePreview() {
    AmigoThemeLight {
        NetworkImage(url = "https://thispersondoesnotexist.com/image")
    }
}