package org.ossiaustria.amigobox.ui.timeline.content

import android.content.Context
import android.speech.tts.TextToSpeech
import timber.log.Timber
import java.util.*

class AdHocSpeaker(context: Context) : TextToSpeech.OnInitListener {
    private val textToSpeech = TextToSpeech(context, this, "com.google.android.tts")
    private var prepared = false

    // TODO: Choose gender
    override fun onInit(status: Int) {
        try {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.GERMAN
                textToSpeech.setSpeechRate(1.0F)
                val voices = textToSpeech.voices
                val filter = voices.filter { it.locale == Locale.GERMANY }
                textToSpeech.voice = filter.random()
                prepared = true
            }
        } catch (e: Exception) {
            Timber.e(e, "Cannot use TTS")
        }
    }

    fun speak(text: CharSequence) {
        if (prepared) {
            textToSpeech.stop()
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun stop() {
        if (prepared) {
            textToSpeech.stop()
        }
    }
}