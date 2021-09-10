package org.ossiaustria.amigobox.ui.contacts

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.ossiaustria.amigobox.ui.UIConstants.ListCard
import org.ossiaustria.amigobox.ui.UIConstants.ListCardContact
import org.ossiaustria.amigobox.ui.commons.NetworkImage
import org.ossiaustria.amigobox.ui.commons.NotFoundImage


@Composable
fun LoadPersonCardContent(name: String, url: String?) {
    Row(
        modifier = Modifier
            .height(ListCard.AVATAR_IMAGE_HEIGHT)
            .width(ListCard.CARD_WIDTH),
    ) {
        if (url.isNullOrBlank()) {
            NotFoundImage()
            // https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50?s=200
        } else {
            NetworkImage(url = url)
        }
    }
    Row(
        modifier = Modifier
            .padding(ListCard.TEXT_PADDING)
            .height(ListCardContact.TEXT_HEIGHT),
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.h2,
            fontSize = ListCardContact.NAME_FONT_SIZE,
        )
    }
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


