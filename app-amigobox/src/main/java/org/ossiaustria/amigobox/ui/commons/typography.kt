package org.ossiaustria.amigobox.ui.commons

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.ossiaustria.amigobox.R

val Sans = FontFamily(
    Font(R.font.dm_serif_display_regular, FontWeight.W400),
)

val Karla = FontFamily(
    Font(R.font.karla_bold, FontWeight.W700),
    Font(R.font.karla_variable_font_wght, FontWeight.W400)
)

val MyTypography = Typography(
    h1 = TextStyle(
        fontFamily = Sans,
        fontWeight = FontWeight.W400,
        fontSize = 72.sp
    ),
    h2 = TextStyle(
        fontFamily = Sans,
        fontWeight = FontWeight.W400,
        fontSize = 64.sp
    ),
    h3 = TextStyle(
        fontFamily = Sans,
        fontWeight = FontWeight.W400,
        fontSize = 53.sp
    ),
    caption = TextStyle(
        fontFamily = Karla,
        fontWeight = FontWeight.W700,
        fontSize = 22.sp
    ),
    body1 = TextStyle(
        fontFamily = Karla,
        fontWeight = FontWeight.W400,
        fontSize = 22.sp
    )
)
//MaterialTheme(typography = MyTypography)
