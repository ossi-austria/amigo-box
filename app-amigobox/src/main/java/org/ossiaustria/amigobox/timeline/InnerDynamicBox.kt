package org.ossiaustria.amigobox.timeline

import SendableCard
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import org.ossiaustria.amigobox.ui.autoplay.AutoState
import org.ossiaustria.amigobox.ui.autoplay.AutoplayCommons
import org.ossiaustria.amigobox.ui.commons.AmigoThemeLight
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.Message
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.Sendable
import org.ossiaustria.lib.domain.models.enums.MemberType
import java.util.*
import java.util.UUID.randomUUID
import kotlin.time.ExperimentalTime

@ExperimentalTime
@Composable
fun InnerDynamicBox(
    sendables: List<Sendable>,
    toAlbum: (Album) -> Unit,
    toHome: () -> Unit,
    cancelTimer: () -> Unit,
    currentIndex: Int?,
    setGalleryIndex: (Int) -> Unit,
    setAutoState: (AutoState) -> Unit,
    time: String,
    autoState: AutoState?,
    centerPerson: Person,
    toCall: (Person) -> Unit,
    findPerson: (UUID) -> Person?,
    autoplay: AutoplayCommons
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LazyRow(
        modifier = Modifier.fillMaxSize(),
        state = listState
    ) {

        if (!sendables.isNullOrEmpty()) {
            items(items = sendables, itemContent = { sendable ->

                Column(modifier = Modifier.fillParentMaxWidth()) {
                    SendableCard(
                        sendable,
                        toAlbum,
                        centerPerson,
                        toCall,
                        findPerson,
                    )
                }
            })
        }
        coroutineScope.launch {
            autoplay.goToSendable(
                cancelTimer,
                listState,
                currentIndex,
                sendables,
                toHome
            )
        }
        autoplay.handleSendables(
            setGalleryIndex,
            setAutoState,
            time,
            currentIndex,
            autoState
        )
    }
}

@ExperimentalTime
@Preview
@Composable
fun InnerDynamicBoxPreview() {
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
        InnerDynamicBox(
            sendables = sendables,
            toAlbum = {},
            toHome = {},
            cancelTimer = {},
            currentIndex = 1,
            setGalleryIndex = {},
            setAutoState = {},
            time = "30:30",
            autoState = AutoState.CHANGE,
            centerPerson = person,
            toCall = {},
            findPerson = { person },
            autoplay = AutoplayCommons()
        )
    }
}