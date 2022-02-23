package org.ossiaustria.amigobox.ui.timeline.content

import ProfileImage
import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil.annotation.ExperimentalCoilApi
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.TextAndIconButton
import org.ossiaustria.amigobox.ui.commons.images.NetworkImage
import org.ossiaustria.lib.domain.models.Message
import org.ossiaustria.lib.domain.models.Person
import java.util.*

class AdHocSpeaker(context: Context) : TextToSpeech.OnInitListener {
    private val textToSpeech = TextToSpeech(context, this, "com.google.android.tts")
    private var prepared = false

    // TODO: Choose gender
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.language = Locale.GERMAN
            textToSpeech.setSpeechRate(0.8f)
            val voices = textToSpeech.voices
            val filter = voices.filter { it.locale == Locale.GERMANY }
            textToSpeech.voice = filter.random()
            prepared = true
        }
    }

    fun speak(text: CharSequence) {
        if (prepared) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }
}

@ExperimentalCoilApi
@Composable
fun MessageContent(message: Message, findPerson: (UUID) -> Person?) {
    val speaker = AdHocSpeaker(LocalContext.current)
    val person = findPerson(message.senderId)
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.padding(UIConstants.Defaults.INNER_PADDING),
            contentAlignment = Alignment.Center
        ) {
            if (message.multimedia != null && message.multimedia!!.absoluteMediaUrl() != null) {
                NetworkImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(UIConstants.ListCard.IMAGE_HEIGHT),
                    url = message.multimedia!!.absoluteMediaUrl() ?: "",
                    contentScale = ContentScale.Crop
                )
            } else {
                ProfileImage(
                    url = person?.absoluteAvatarUrl(),
                    contentScale = ContentScale.Crop,
                )
            }
        }
        Column {
            var textName = stringResource(R.string.unknown_person)
            val name = person?.name
            if (name != null) {
                textName = name
            }
            Text(
                text = stringResource(R.string.message_from, textName),
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(
                    vertical = UIConstants.Defaults.INNER_PADDING
                )
            )
            Text(
                text = message.text.trim(),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(
                    vertical = UIConstants.Defaults.INNER_PADDING
                )
            )
            TextAndIconButton(iconId = R.drawable.ic_microphone, text = "Vorlesen") {
                speaker.speak(message.text)
            }
        }
    }
}
