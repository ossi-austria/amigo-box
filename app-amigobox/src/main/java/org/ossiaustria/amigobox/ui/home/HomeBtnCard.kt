package org.ossiaustria.amigobox.ui.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import org.ossiaustria.amigobox.ui.UIConstants

@Composable
fun HomeBtnCard(@DrawableRes imageResId: Int, buttonDescription: String) {

    Row(
        modifier = Modifier
            .height(UIConstants.HomeButtonsCard.IMAGE_HEIGHT)
            .width(UIConstants.HomeButtonsCard.IMAGE_WIDTH),
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = imageResId),
            contentDescription = null,
            alignment = Alignment.Center,
        )
    }
    Row(
        modifier = Modifier
            .padding(UIConstants.HomeButtonsCard.TEXT_PADDING)
            .height(UIConstants.HomeButtonsCard.TEXT_HEIGHT),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = buttonDescription,
            textAlign = TextAlign.Center,
            fontSize = UIConstants.HomeButtonsCard.FONT_SIZE,
        )
    }
}