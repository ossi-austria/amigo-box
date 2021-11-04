package org.ossiaustria.amigobox.ui.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import org.ossiaustria.amigobox.ui.UIConstants

@Composable
fun HomeBtnCard(@DrawableRes imageResId: Int, buttonDescription: String, showCounter: Boolean) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .height(UIConstants.HomeButtonsCard.CARD_HEIGHT)
            .width(UIConstants.HomeButtonsCard.CARD_WIDTH),
    ) {
        Row(
            modifier = Modifier
                .width(UIConstants.HomeButtonsCard.IMAGE_WIDTH)
                .height(UIConstants.HomeButtonsCard.IMAGE_HEIGHT),
            horizontalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        MaterialTheme.colors.primary,
                        BlendMode.SrcIn
                    )
                )
                if (showCounter) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = UIConstants.HomeButtonsCard.NOTIFICATION_PADDING_TOP,
                                end = UIConstants.HomeButtonsCard.NOTIFICATION_PADDING_END
                            ),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(MaterialTheme.colors.secondary)
                                .size(UIConstants.HomeButtonsCard.NOTIFICATION_SIZE),
                        ) {
                            Text(
                                text = "0",
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.onSecondary
                            )
                        }
                    }
                }
            }
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
                style = MaterialTheme.typography.caption,
            )
        }
    }
}

