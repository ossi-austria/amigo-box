package org.ossiaustria.amigobox.ui.commons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants

// The main Amigo Button
@Composable
fun HelperButton(
    @DrawableRes iconId: Int?,
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = MaterialTheme.colors.onPrimary,
    @DrawableRes endIconId: Int? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .alpha(if (enabled) 1F else 0F)
            .padding(8.dp)
            .clickable(onClick = onClick, enabled = enabled)
            .clip(RoundedCornerShape(UIConstants.SmallButtons.ROUNDED_CORNER))
            .background(color = backgroundColor)
            .padding(UIConstants.SmallButtons.PADDING),

        ) {
        if (iconId != null)
            Image(
                modifier = Modifier
                    .width(UIConstants.SmallButtons.ICON_SIZE)
                    .height(UIConstants.SmallButtons.ICON_SIZE)
                    .padding(UIConstants.SmallButtons.PADDING),
                painter = painterResource(id = iconId),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    contentColor,
                    BlendMode.SrcIn
                ),
            )

        Text(
            modifier = Modifier.padding(UIConstants.BigButtons.ICON_PADDING),
            text = text,
            style = MaterialTheme.typography.body2,
            color = contentColor,
        )
        if (endIconId != null) {
            Image(
                modifier = Modifier
                    .width(UIConstants.BigButtons.ICON_SIZE)
                    .height(UIConstants.BigButtons.ICON_SIZE)
                    .padding(UIConstants.BigButtons.ICON_PADDING),
                painter = painterResource(id = endIconId),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    contentColor,
                    BlendMode.SrcIn
                ),
            )
        }
    }
}

@Preview
@Composable
fun HelperButtonPreview_BottomStart() {
    PreviewTheme {
        HelperButton(
            iconId = R.drawable.ic_image_light,
            text = "text",
            onClick = {}
        )
    }
}

@Preview
@Composable
fun HelperButtonPreview_LongTest() {
    PreviewTheme {
        HelperButton(
            iconId = R.drawable.ic_image_light,
            text = "Das ist ein sehr langer Text, mal sehen was Umrbuch macht",
            onClick = {}
        )
    }
}

@Preview
@Composable
fun HelperButtonPreview_Secondary() {
    PreviewTheme {
        HelperButton(
            iconId = R.drawable.ic_image_light,
            backgroundColor = MaterialTheme.colors.secondary,
            text = "text",
            onClick = {}
        )
    }
}
