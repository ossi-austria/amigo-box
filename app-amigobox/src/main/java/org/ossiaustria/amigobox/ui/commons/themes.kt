package org.ossiaustria.amigobox.ui.commons

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
/** this class defines theme for the whole application. The colors and typography is set here.*/
@Composable
fun MaterialThemeLight(content: @Composable () -> Unit) {

    MaterialTheme(
        colors = LightColors,
        typography = MyTypography,
        content = content
    )
}

val LightColors = lightColors(
    primary = orange,
    surface = white,
    onPrimary = textColor,
    secondary = darkBlue,
    onSecondary = white,
    background = lightGrey,
    onBackground = textColor,
)


