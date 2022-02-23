package org.ossiaustria.amigobox.ui.commons

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.ossiaustria.amigobox.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

object TimeUtils {
    private val datetimeFormat = SimpleDateFormat("dd. MMMM yyyy - HH:mm", Locale.GERMAN)
    fun fullDate(dateTime: Date): String = datetimeFormat.format(dateTime)
}

@Composable
fun durationToString(duration: Long?): String {
    return if (duration != null) {
        val durationInSeconds = (duration / 1000)
        val minutes = (durationInSeconds / 60).absoluteValue
        val seconds = (durationInSeconds % 60)
        ("${twoDigitFormat(minutes)}:${twoDigitFormat(seconds)}")
    } else stringResource(R.string.unknown_duration)
}

fun twoDigitFormat(number: Long): String {
    return String.format(Locale.GERMAN, "%02d", number)
}