package org.ossiaustria.amigobox.ui.commons

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.LayoutDirection


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
    onClick: () -> Unit,
    text: String,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colors.primary,
    )
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
            contentColor = scrollTextColor(type, scrollState)),
    ) {
        Text(text = text)
    }
}

@Composable
fun scrollTextColor(type: ScrollButtonType, scrollState: ScrollState): Color{
    if (type == ScrollButtonType.PREVIOUS){
        if (scrollState.value == 0){
            return Color.Gray
        }
        else return MaterialTheme.colors.primary
    }
    else if (type == ScrollButtonType.NEXT){
        if (scrollState.value == scrollState.maxValue){
            return Color.Gray
        }
        else return MaterialTheme.colors.primary
    }
    else{
        return MaterialTheme.colors.primary
    }

}

enum class ScrollButtonType {
    PREVIOUS, NEXT
}


