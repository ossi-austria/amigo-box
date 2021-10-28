package org.ossiaustria.amigobox.ui.commons

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

/** this class defines theme for the whole application. The colors and typography is set here.*/
@Composable
fun AmigoThemeLight(content: @Composable () -> Unit) {

    MaterialTheme(
        colors = LightColors,
        typography = MyTypography,
        content = content
    )
}

@Composable
fun AmigoThemeDark(content: @Composable () -> Unit) {

    MaterialTheme(
        colors = DarkColors,
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
val DarkColors = darkColors(
    primary = orange,
    surface = white,
    onPrimary = textColor,
    secondary = white,
    background = darkBlue,
    onBackground = white,
)


