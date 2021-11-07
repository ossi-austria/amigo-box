package org.ossiaustria.amigobox.calls

import android.content.Context
import android.media.MediaPlayer
import org.ossiaustria.amigobox.R

class PhoneSoundManager {

    private var outgoingSound: MediaPlayer? = null
    private var incomingSound: MediaPlayer? = null

    fun prepare(context: Context) {
        outgoingSound = MediaPlayer.create(context, R.raw.outgoing).apply {
            setVolume(OUTGOING_SOUND_VOLUME, OUTGOING_SOUND_VOLUME)
        }
        incomingSound = MediaPlayer.create(context, R.raw.incoming).apply {
            setVolume(INCOMING_SOUND_VOLUME, INCOMING_SOUND_VOLUME)
        }
    }

    fun playOutgoing() {
        incomingSound?.stop()
        outgoingSound?.isLooping = true
        outgoingSound?.start()
    }

    fun playIncoming() {
        outgoingSound?.stop()
        incomingSound?.isLooping = true
        incomingSound?.start()
    }

    fun release() {
        outgoingSound?.stop()
        incomingSound?.stop()
        outgoingSound?.release()
        incomingSound?.release()
        outgoingSound = null
        incomingSound = null
    }

    fun stopAll() {
        incomingSound?.stop()
        outgoingSound?.stop()
    }

    companion object {
        const val OUTGOING_SOUND_VOLUME = 0.7F
        const val INCOMING_SOUND_VOLUME = 0.9F
    }
}