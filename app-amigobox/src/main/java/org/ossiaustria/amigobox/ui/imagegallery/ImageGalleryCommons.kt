package org.ossiaustria.amigobox.ui.imagegallery

import java.util.concurrent.TimeUnit

enum class GalleryNavState {
    STOP, PLAY
}

enum class AutoState {
    CHANGE, CHANGED
}

object Utility {
    //time to countdown - 1hr - 60secs
    const val TIME_COUNTDOWN: Long = 5000L
    const val TIME_FORMAT = "%02d:%02d"
}

//convert time to milli seconds
fun Long.formatTime(): String = String.format(
    Utility.TIME_FORMAT,
    TimeUnit.MILLISECONDS.toMinutes(this),
    TimeUnit.MILLISECONDS.toSeconds(this) % 60
)