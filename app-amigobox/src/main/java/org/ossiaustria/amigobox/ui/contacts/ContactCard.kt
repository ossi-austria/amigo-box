package org.ossiaustria.amigobox.ui.contacts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import org.ossiaustria.amigobox.ui.UIConstants.ListCard
import org.ossiaustria.amigobox.ui.UIConstants.ListCardContact

@Composable
fun LoadPersonCardContent(personImage: PersonImage) {
    // TODO: change descrpition

    Row(
        modifier = Modifier
            .height(ListCard.AVATAR_IMAGE_HEIGHT)
            .width(ListCard.CARD_WIDTH),
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = personImage.aImageRes),
            contentDescription = null,
        )
    }
    Row(
        modifier = Modifier
            .padding(ListCard.TEXT_PADDING)
            .height(ListCardContact.TEXT_HEIGHT),
    ){
        Text(
            text = personImage.person.name,
            style = MaterialTheme.typography.h2,
            fontSize = ListCardContact.NAME_FONT_SIZE,
        )
    }
}



