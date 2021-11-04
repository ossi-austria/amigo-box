package org.ossiaustria.amigobox.ui.commons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants

@Composable
fun MaterialButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = Color.Blue
    ),
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        colors = colors,
        enabled = enabled,
    ) {
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
fun MaterialButtonPreview() {
    MaterialTheme {
        MaterialButton({}, "text")
    }
}

@Composable
fun NavigationButton(
    text: String,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colors.primary,
    ),
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        colors = colors,
    ) {
        Text(text = text)
    }

}

@Composable
fun ScrollNavigationButton(
    type: ScrollButtonType,
    text: String,
    scrollState: ScrollState,
    onClick: () -> Unit,
) {
    val scrollButtonRowModifier = Modifier
        .height(UIConstants.ScrollNavigationButton.ROW_HEIGHT)
        .background(MaterialTheme.colors.background)

    val imageModifier = Modifier
        .size(UIConstants.ScrollNavigationButton.IMAGE_SIZE)
        .padding(end = UIConstants.ScrollNavigationButton.IMAGE_PADDING)

    Card(
        modifier = Modifier
            .clickable(onClick = onClick)
            .height(UIConstants.ScrollNavigationButton.CARD_HEIGHT)
            .width(UIConstants.ScrollNavigationButton.CARD_WIDTH),
        elevation = UIConstants.ScrollableCardList.CARD_ELEVATION,
        contentColor = scrollTextColor(type, scrollState),
    ) {
        when (type) {
            ScrollButtonType.PREVIOUS ->
                Row(
                    modifier = scrollButtonRowModifier,
                    Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_left),
                        contentDescription = null,
                        modifier = imageModifier,
                        colorFilter = ColorFilter.tint(
                            scrollTextColor(type, scrollState),
                            BlendMode.SrcIn
                        ),
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.h4,
                    )
                }
            ScrollButtonType.NEXT ->
                Row(
                    modifier = scrollButtonRowModifier,
                    Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.h4,
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = null,
                        modifier = imageModifier,
                        colorFilter = ColorFilter.tint(
                            scrollTextColor(type, scrollState),
                            BlendMode.SrcIn
                        ),
                    )
                }
        }
    }
}

@Composable
fun scrollTextColor(type: ScrollButtonType, scrollState: ScrollState): Color {
    if (type == ScrollButtonType.PREVIOUS) {
        if (scrollState.value == 0) {
            return Color.Gray
        } else return MaterialTheme.colors.secondary
    } else if (type == ScrollButtonType.NEXT) {
        if (scrollState.value == scrollState.maxValue) {
            return Color.Gray
        } else return MaterialTheme.colors.secondary
    } else {
        return MaterialTheme.colors.secondary
    }

}

enum class ScrollButtonType {
    PREVIOUS, NEXT
}

@Composable
fun IconButtonSmall(
    @DrawableRes resourceId: Int,
    backgroundColor: Color,
    fillColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable(onClick = onClick)
            .size(UIConstants.SmallButtons.BUTTON_SIZE)
            .padding(UIConstants.SmallButtons.CARD_PADDING),
        elevation = UIConstants.ScrollableCardList.CARD_ELEVATION

    ) {
        IconButtonShape(Modifier, fillColor, backgroundColor)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .size(UIConstants.SmallButtons.ICON_SIZE),
                painter = painterResource(id = resourceId),
                contentDescription = null,
            )
        }

    }

}

//Source: https://medium.com/google-developer-experts/exploring-jetpack-compose-canvas-the-power-of-drawing-8cc60815babe
@Composable
fun IconButtonShape(
    modifier: Modifier,
    fillColor: Color,
    backgroundColor: Color
) {
    Canvas(
        modifier = modifier
            .size(UIConstants.SmallButtons.BUTTON_SIZE)
            .background(backgroundColor),
        onDraw = {
            drawCircle(
                color = fillColor,
                radius = size.width / 2,
                center = center
            )
            drawRect(
                color = fillColor,
                topLeft = Offset(0F, size.width - size.width / 2),
                size = Size(size.height / 2, size.width / 2)
            )
        }
    )
}

@Composable
fun TextAndIconButton(
    @DrawableRes resourceId: Int,
    buttonDescription: String,
    backgroundColor: Color,
    contentColor: Color,
    bottomStart: Boolean = true,
    topStart: Boolean = false,
    buttonWidth: Dp = UIConstants.BigButtons.BUTTON_WIDTH,
    onClick: () -> Unit,

    ) {
    Card(
        modifier = Modifier
            .clickable(onClick = onClick)
            .height(UIConstants.BigButtons.BUTTON_HEIGHT)
            .width(buttonWidth)
            .padding(UIConstants.BigButtons.CARD_PADDING)
            .clip(
                when {
                    bottomStart -> {
                        RoundedCornerShape(
                            topStart = UIConstants.BigButtons.ROUNDED_CORNER,
                            topEnd = UIConstants.BigButtons.ROUNDED_CORNER,
                            bottomEnd = UIConstants.BigButtons.ROUNDED_CORNER,
                        )
                    }
                    topStart -> {
                        RoundedCornerShape(
                            bottomStart = UIConstants.BigButtons.ROUNDED_CORNER,
                            topEnd = UIConstants.BigButtons.ROUNDED_CORNER,
                            bottomEnd = UIConstants.BigButtons.ROUNDED_CORNER,
                        )
                    }
                    else -> {
                        RoundedCornerShape(UIConstants.BigButtons.ROUNDED_CORNER)
                    }
                }
            ),
        contentColor = MaterialTheme.colors.onPrimary,
        backgroundColor = backgroundColor
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Image(
                modifier = Modifier
                    .width(UIConstants.BigButtons.ICON_SIZE)
                    .height(UIConstants.BigButtons.ICON_SIZE)
                    .padding(UIConstants.BigButtons.ICON_PADDING),
                painter = painterResource(id = resourceId),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    contentColor,
                    BlendMode.SrcIn
                ),
            )

            Text(
                modifier = Modifier
                    .padding(UIConstants.BigButtons.ICON_PADDING),
                text = buttonDescription,
                style = MaterialTheme.typography.body2,
                color = contentColor,
            )
        }
    }
}


