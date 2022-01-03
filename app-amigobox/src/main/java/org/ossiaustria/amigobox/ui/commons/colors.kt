package org.ossiaustria.amigobox.ui.commons

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AmigoColors {

    val white = Color.White
    val charcoal = Color(0xFF171717)
    val lightGrey = Color(0xFFF2F6F7)
    val mistyOcean = Color(0xFF075760)
    val darkNavySmoke = Color(0xFF00292E)
    val orange = Color(0xFFFFBA5F)
    val redVariance = Color(0xFFFF5F5F)
}

@Composable
fun navigationTextColor(type: NavigationButtonType, itemIndex: Int?, listSize: Int): Color {
    return if (type == NavigationButtonType.PREVIOUS) {
        if (itemIndex == 0) {
            Color.Gray
        } else MaterialTheme.colors.primary
    } else if (type == NavigationButtonType.NEXT) {
        if (itemIndex != null) {
            if (itemIndex + 1 == listSize) {
                Color.Gray
            } else MaterialTheme.colors.primary
        } else MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.primary
    }
}