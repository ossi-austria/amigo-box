import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.commons.AmigoStyle
import org.ossiaustria.amigobox.ui.commons.AmigoThemeLight
import org.ossiaustria.amigobox.ui.commons.textResString

@Composable
fun NotFoundImage(
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    Image(
        painter = painterResource(R.drawable.ic_image_light),
        contentDescription = textResString(altText = "Image not found"),
        modifier = modifier.then(AmigoStyle.Images.roundBorders()),
        contentScale = contentScale,
    )
}

@Composable
@Preview(showBackground = true)
fun NotFoundImagePreview() {
    AmigoThemeLight {
        NotFoundImage()
    }
}