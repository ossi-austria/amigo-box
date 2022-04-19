package org.ossiaustria.amigobox.ui.timeline.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.annotation.ExperimentalCoilApi
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.PreviewTheme
import org.ossiaustria.amigobox.ui.commons.TextAndIconButton
import org.ossiaustria.amigobox.ui.commons.durationToString
import org.ossiaustria.amigobox.ui.commons.images.ProfileImage
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.CallState
import org.ossiaustria.lib.domain.models.enums.CallType
import java.util.*

@ExperimentalCoilApi
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
        val otherPerson = findPerson(call.otherPersonId(centerPerson.id))
        Box(
            modifier = Modifier.padding(UIConstants.Defaults.INNER_PADDING),
            contentAlignment = Alignment.Center
        ) {
            ProfileImage(
                url = otherPerson?.absoluteAvatarUrl(),
                contentScale = ContentScale.Crop,
            )
        }
        Column {

            val textName = otherPerson?.name ?: stringResource(id = R.string.unknown_person)

            val text = stringResource(
                R.string.you_had_a_call,
                durationToString(call.duration),
                textName
            )
            Text(
                text = text,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(
                    vertical = UIConstants.Defaults.INNER_PADDING
                )
            )
            TextAndIconButton(
                iconId = R.drawable.ic_phone_call,
                text = stringResource(R.string.call_back_button),
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                bottomStart = false,
                topStart = true,
            ) {
                findPerson(call.otherPersonId(centerPerson.id))?.let { toCall(it) }
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
@Preview
fun CallContentPreview() {
    PreviewTheme {
        val call = Call(
            id = UUID.randomUUID(),
            senderId = ContentMocks.otherPerson.id,
            receiverId = ContentMocks.centerPerson.id,
            callType = CallType.VIDEO,
            callState = CallState.ACCEPTED,
            startedAt = Date()
        )
        CallContent(
            call = call,
            centerPerson = ContentMocks.centerPerson,
            findPerson = { ContentMocks.otherPerson },
            toCall = {}
        )
    }
}
