package org.ossiaustria.amigobox.calls

import android.content.Context
import android.media.MediaPlayer
import org.ossiaustria.amigobox.R

class PhoneSoundManager {

    private var callingSound: MediaPlayer? = null
    private var incomingSound: MediaPlayer? = null

    fun prepare(context: Context) {
        callingSound = MediaPlayer.create(context, R.raw.outgoing).apply {
            setVolume(OUTGOING_SOUND_VOLUME, OUTGOING_SOUND_VOLUME)
        }
        incomingSound = MediaPlayer.create(context, R.raw.incoming).apply {
            setVolume(INCOMING_SOUND_VOLUME, INCOMING_SOUND_VOLUME)
        }
    }

    fun playOutgoing() {
        incomingSound?.stop()
        callingSound?.isLooping = true
        callingSound?.start()
    }

    fun playIncoming() {
        callingSound?.stop()
        incomingSound?.isLooping = true
        incomingSound?.start()
    }

    fun release() {
        callingSound?.release()
        incomingSound?.release()
        callingSound = null
        incomingSound = null
    }

    fun stopAll() {
        incomingSound?.stop()
        callingSound?.stop()
    }

    companion object {
        const val OUTGOING_SOUND_VOLUME = 0.7F
        const val INCOMING_SOUND_VOLUME = 0.9F
    }
}