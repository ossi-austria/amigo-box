package org.ossiaustria.amigobox.ui.commons

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

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