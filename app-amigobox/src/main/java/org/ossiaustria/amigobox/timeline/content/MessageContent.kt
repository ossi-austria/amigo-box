package org.ossiaustria.amigobox.timeline.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.lib.domain.models.Message
import org.ossiaustria.lib.domain.models.Person
import java.util.*

@Composable
fun MessageContent(message: Message, findPerson: (UUID) -> Person?) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(
                start = UIConstants.TimelineFragment.PROFIL_IMAGE_COLUMN_PADDING_START,
                end = UIConstants.TimelineFragment.PROFIL_IMAGE_COLUMN_PADDING_END
            )
        ) {
            //TODO: temporary solution for design only, should use profileImage and url to upload profile picture
            Image(
                painter = painterResource(id = R.drawable.image_gusti_amigo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(UIConstants.TimelineFragment.IMAGE_SIZE)
                    .padding(UIConstants.ProfileImage.IMAGE_PADDING)
                    .clip(CircleShape)
                    .border(
                        UIConstants.ProfileImage.BORDER_WIDTH,
                        MaterialTheme.colors.surface,
                        CircleShape
                    )
            )
        }
        Column {
            var textName = stringResource(R.string.unknown_person)
            val name = findPerson(message.senderId)?.name
            if (name != null) {
                textName = name
            }
            Text(
                text = stringResource(R.string.message_from, textName),
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(
                    bottom = UIConstants.TimelineFragment.CONTENT_TEXT_PADDING_BOTTOM,
                    top = UIConstants.TimelineFragment.CONTENT_TEXT_PADDING_TOP
                )
            )
            Text(
                text = message.text,
                style = MaterialTheme.typography.body1
            )
        }
    }
}
