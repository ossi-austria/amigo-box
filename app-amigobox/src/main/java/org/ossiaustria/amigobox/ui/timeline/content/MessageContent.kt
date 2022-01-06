package org.ossiaustria.amigobox.ui.timeline.content

import ProfileImage
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil.annotation.ExperimentalCoilApi
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.lib.domain.models.Message
import org.ossiaustria.lib.domain.models.Person
import java.util.*

@ExperimentalCoilApi
@Composable
fun MessageContent(message: Message, findPerson: (UUID) -> Person?) {
    val person = findPerson(message.senderId)
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(UIConstants.TimelineFragment.BIGGER_PADDING)
        ) {
            ProfileImage(
                url = person?.absoluteAvatarUrl(),
                contentScale = ContentScale.Crop,
            )
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
                    vertical = UIConstants.TimelineFragment.PADDING
                )
            )
            Text(
                text = message.text,
                style = MaterialTheme.typography.body1
            )
        }
    }
}
