package org.ossiaustria.amigobox.ui.contacts

import NotFoundImage
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants.Defaults.CARD_WIDTH
import org.ossiaustria.amigobox.ui.UIConstants.Defaults.TEXT_PADDING
import org.ossiaustria.amigobox.ui.UIConstants.ListCard
import org.ossiaustria.amigobox.ui.commons.images.NetworkImage

@Composable
fun LoadPersonCardContent(name: String, url: String?) {
    Row(
        modifier = Modifier
            .width(CARD_WIDTH),
    ) {
        if (url.isNullOrBlank()) {
            NotFoundImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ListCard.IMAGE_HEIGHT),
            )
        } else {
            NetworkImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ListCard.IMAGE_HEIGHT),
                url = url,
                contentScale = ContentScale.Crop
            )
        }
    }
    Row(
        modifier = Modifier
            .padding(TEXT_PADDING)
            .width(CARD_WIDTH),
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.h4,
        )
    }
    Image(
        painterResource(id = R.drawable.ic_rectangle_264),
        modifier = Modifier
            .height(ListCard.RECT_HEIGHT)
            .padding(start = TEXT_PADDING),
        contentDescription = null
    )
}

@Composable
@Preview
fun PreviewLoadPersonCardContent1() {
    LoadPersonCardContent("Lukas", "https://thispersondoesnotexist.com/image")
}

@Composable
@Preview
fun PreviewLoadPersonCardContent2() {
    LoadPersonCardContent("No image", null)
}


