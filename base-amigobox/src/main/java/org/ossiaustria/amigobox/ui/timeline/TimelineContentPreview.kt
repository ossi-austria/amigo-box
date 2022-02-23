package org.ossiaustria.amigobox.ui.timeline

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.ossiaustria.amigobox.ui.autoplay.TimerState
import org.ossiaustria.amigobox.ui.commons.AmigoThemeLight
import org.ossiaustria.lib.domain.models.Message
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.MemberType
import java.util.UUID.randomUUID
import kotlin.time.ExperimentalTime

@ExperimentalTime
@Preview
@Composable
fun TimelineContentPreview() {
    val sendables = listOf(
        Message(
            randomUUID(),
            senderId = randomUUID(),
            receiverId = randomUUID(),
            text = "Message 1"
        ),
        Message(
            randomUUID(),
            senderId = randomUUID(),
            receiverId = randomUUID(),
            text = "Message 2"
        )
    )
    val person = Person(
        randomUUID(),
        groupId = randomUUID(),
        memberType = MemberType.MEMBER,
        name = "Message 2", avatarUrl = null
    )
    AmigoThemeLight {
        TimelineContent(
            currentIndex = 1,
            timerState = TimerState.STOP,
            centerPerson = person,
            sendables = sendables,
            findPerson = { person },
            toHome = {},
            toAlbum = {},
            toCall = {},
            onPreviousPressed = {},
            onNextPressed = {},
            onStartStopPressed = {},
        )
    }
}