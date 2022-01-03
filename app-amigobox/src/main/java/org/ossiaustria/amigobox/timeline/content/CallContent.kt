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
import androidx.compose.ui.tooling.preview.Preview
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.PreviewTheme
import org.ossiaustria.amigobox.ui.commons.TextAndIconButton
import org.ossiaustria.amigobox.ui.commons.durationToString
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.CallState
import java.util.*
import kotlin.time.ExperimentalTime

@ExperimentalTime
@Composable
fun CallContent(
    call: Call,
    centerPerson: Person,
    findPerson: (UUID) -> Person?,
    toCall: (Person) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(
                start = UIConstants.TimelineFragment.PROFIL_IMAGE_COLUMN_PADDING_START,
                end = UIConstants.TimelineFragment.PROFIL_IMAGE_COLUMN_PADDING_END
            )
        ) {
            //TODO: temporary solution for design only, should use profilImage and url to upload profile picture
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
            Text(
                modifier = Modifier.padding(
                    bottom = UIConstants.TimelineFragment.CONTENT_TEXT_PADDING_BOTTOM,
                    top = UIConstants.TimelineFragment.CONTENT_TEXT_PADDING_TOP
                ),
                text = stringResource(R.string.good_talk),
                style = MaterialTheme.typography.caption
            )
            var textName = stringResource(id = R.string.unknown_person)

            val nameSender = findPerson(call.senderId)?.name
            val nameReceiver = findPerson(call.receiverId)?.name
            if (centerPerson.id == call.receiverId && nameSender != null) {
                textName = nameSender
            } else if (centerPerson.id == call.senderId && nameReceiver != null) {
                textName = nameReceiver
            }

            val text = if (call.callState == CallState.FINISHED) {
                stringResource(
                    R.string.you_had_a_call,
                    durationToString(call.duration),
                    textName
                )
            } else {
                // TODO: Add other states?
                "${call.callState}"
            }
            Text(
                text = text,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(
                    bottom = UIConstants.TimelineFragment.BOTTOM_PADDING
                )
            )
            TextAndIconButton(
                resourceId = R.drawable.ic_phone_call,
                text = stringResource(R.string.call_back_button),
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                bottomStart = false,
                topStart = true,
                buttonWidth = UIConstants.TimelineFragment.BUTTON_WIDTH
            ) {
                findPerson(call.otherPersonId(centerPerson.id))?.let { toCall(it) }
            }
        }
    }
}

@ExperimentalTime
@Composable
@Preview
fun CallContentPreview() {
    PreviewTheme {
        CallContent(
            ContentMocks.call,
            ContentMocks.centerPerson,
            { ContentMocks.otherPerson },
            {},
        )
    }
}
