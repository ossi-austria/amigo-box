package org.ossiaustria.amigobox.ui.imagegallery

import java.util.concurrent.TimeUnit

enum class GalleryNavState {
    STOP, PLAY
}

enum class AutoState {
    CHANGE, CHANGED
}

object CountdownFormat {
    //time to countdown here 5000 milliseconds = 5 seconds
    const val TIME_COUNTDOWN: Long = 5000L
    const val TIME_FORMAT = "%02d:%02d"
}

//convert time to milli seconds
fun Long.formatTime(): String = String.format(
    CountdownFormat.TIME_FORMAT,
    TimeUnit.MILLISECONDS.toMinutes(this),
    TimeUnit.MILLISECONDS.toSeconds(this) % 60
)