package org.ossiaustria.amigobox.ui.commons

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.lightColors
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

/** this class defines theme for the whole application. The colors and typography is set here.*/
@Composable
fun AmigoThemeLight(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = AmigoLightColors,
        typography = MyTypography,
        content = content
    )
}

val AmigoLightColors = lightColors(
    surface = AmigoColors.white,
    primary = AmigoColors.orange,
    onPrimary = AmigoColors.darkNavySmoke,
    secondary = AmigoColors.mistyOcean,
    secondaryVariant = AmigoColors.mistyOcean,
    onSecondary = AmigoColors.white,
    background = AmigoColors.lightGrey,
    onBackground = AmigoColors.charcoal,
    error = AmigoColors.redVariance
)

@Composable
fun PreviewTheme(content: @Composable () -> Unit) {
    AmigoThemeLight {
        Surface(color = MaterialTheme.colors.background, content = content)
    }
}