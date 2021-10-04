package org.ossiaustria.amigobox.ui.commons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import org.ossiaustria.amigobox.ui.UIConstants

// reuse a Composable - there are no styles
@Composable
fun MaterialButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier? = null,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = Color.Blue
    )
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier ?: Modifier.fillMaxWidth(),
        colors = colors
    ) {
        Text(text = text)
    }
}

@Composable
fun NavigationButton(
    modifier: Modifier = Modifier,
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
    onClick: () -> Unit,
    type: ScrollButtonType,
    text: String,
    scrollState: ScrollState
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = scrollTextColor(type, scrollState)
        ),
    ) {
        Text(text = text)
    }
}

@Composable
fun scrollTextColor(type: ScrollButtonType, scrollState: ScrollState): Color {
    if (type == ScrollButtonType.PREVIOUS) {
        if (scrollState.value == 0) {
            return Color.Gray
        } else return MaterialTheme.colors.primary
    } else if (type == ScrollButtonType.NEXT) {
        if (scrollState.value == scrollState.maxValue) {
            return Color.Gray
        } else return MaterialTheme.colors.primary
    } else {
        return MaterialTheme.colors.primary
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
    bottomStart: Boolean,
    topStart: Boolean,
    onClick: () -> Unit,

    ) {
    Card(
        modifier = Modifier
            .clickable(onClick = onClick)
            .width(UIConstants.BigButtons.BUTTON_WIDTH)
            .height(UIConstants.BigButtons.BUTTON_HEIGHT)
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
        backgroundColor = backgroundColor,

        ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
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
                )

                Text(
                    modifier = Modifier
                        .padding(UIConstants.BigButtons.ICON_PADDING),
                    text = buttonDescription,
                    style = MaterialTheme.typography.caption
                )
            }
        }

    }
}


