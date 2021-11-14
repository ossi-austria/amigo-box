package org.ossiaustria.amigobox.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.autoplay.AutoplayCommons
import org.ossiaustria.amigobox.ui.autoplay.GalleryNavState
import org.ossiaustria.amigobox.ui.commons.AmigoStyle
import org.ossiaustria.amigobox.ui.commons.AmigoThemeLight
import org.ossiaustria.amigobox.ui.commons.TimeUtils
import org.ossiaustria.lib.domain.models.AlbumShare
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.Message
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.Sendable
import org.ossiaustria.lib.domain.models.enums.MemberType
import java.util.*
import kotlin.time.ExperimentalTime

@ExperimentalTime
@Composable
fun OuterStaticBox(
    toHome: () -> Unit,
    cancelTimer: () -> Unit,
    setGalleryIndex: (Int) -> Unit,
    startTimer: () -> Unit,
    currentIndex: Int?,
    navigationState: GalleryNavState?,
    setNavigationState: (GalleryNavState) -> Unit,
    pauseTimer: () -> Unit,
    /*
     TODO: refactor. you dont all sendables, you need:
     - currentSendable: Sendable
     - currentIndex: Int
     - size: Int
     */
    sendables: List<Sendable>,
    centerPerson: Person?,
    findName: (UUID) -> String?,
    autoplay: AutoplayCommons
) {
    //This is the static Background Box
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            val listSize = sendables.size
            Column(
                // TODO: Constant width
                modifier = Modifier.width(800.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Text showing which Sendable is shown from List
                if (!sendables.isNullOrEmpty()) {
                    if ((currentIndex != null) && (currentIndex != sendables.size)) {
                        Text(
                            text = (currentIndex + 1).toString() + "/" + listSize.toString(),
                            Modifier
                                .padding(
                                    top = AmigoStyle.Dim.D,
                                    start = AmigoStyle.Dim.D,
                                    bottom = AmigoStyle.Dim.C
                                )
                                .background(MaterialTheme.colors.primary, shape = CircleShape)
                                .height(UIConstants.TimelineFragment.INDEX_BOX_HEIGHT)
                                .width(UIConstants.TimelineFragment.INDEX_BOX_WIDTH),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onPrimary,
                            style = MaterialTheme.typography.body1
                        )
                        // TODO: dont rely on list, if you just need one item
                        val sendable = sendables[currentIndex]
                        Text(
                            text = stringResource(
                                R.string.fullDateString,
                                TimeUtils.fullDate(sendable.createdAt)
                            ),
                            Modifier.padding(
                                start = AmigoStyle.Dim.D,
                                bottom = AmigoStyle.Dim.A
                            )
                        )

                        var textNewItem = stringResource(R.string.new_xxx)
                        var textName = stringResource(R.string.unknown_person)
                        val senderName = findName(sendable.senderId)
                        when (sendable) {
                            is AlbumShare -> {
                                textNewItem = stringResource(R.string.new_album)
                                if (senderName != null) {
                                    textName = senderName.toString()
                                }
                            }
                            is Call -> {
                                textNewItem = stringResource(R.string.new_call)
                                if (centerPerson != null) {
                                    val findName1 =
                                        findName(sendable.otherPersonId(centerPerson.id))
                                    if (findName1 != null) {
                                        textName = findName1.toString()
                                    }
                                }
                            }
                            is Message -> {
                                textNewItem = stringResource(R.string.new_message)
                                if (senderName != null) {
                                    textName = senderName.toString()
                                }
                            }
                        }
                        Text(
                            text = stringResource(R.string.from, textNewItem, textName),
                            Modifier.padding(
                                start = AmigoStyle.Dim.D,
                                bottom = AmigoStyle.Dim.A
                            )
                        )
                    }
                }
            }
            HomeHelpButtonColumn(toHome)
        }

        autoplay.TimerNavigationButtonsRow(
            cancelTimer,
            setGalleryIndex,
            startTimer,
            currentIndex,
            navigationState,
            setNavigationState,
            pauseTimer,
            sendables
        )
    }
}

@ExperimentalTime
@Preview
@Composable
fun OuterStaticBoxPreview() {
    val sendables = listOf(
        Message(
            UUID.randomUUID(),
            senderId = UUID.randomUUID(),
            receiverId = UUID.randomUUID(),
            text = "Message 1"
        ),
        Message(
            UUID.randomUUID(),
            senderId = UUID.randomUUID(),
            receiverId = UUID.randomUUID(),
            text = "Message 2"
        )
    )
    val person = Person(
        UUID.randomUUID(),
        groupId = UUID.randomUUID(),
        memberType = MemberType.MEMBER,
        name = "Message 2", avatarUrl = null
    )
    AmigoThemeLight {
        OuterStaticBox(
            sendables = sendables,
            toHome = {},
            cancelTimer = {},
            currentIndex = 1,
            setGalleryIndex = {},
            centerPerson = person,
            findName = { "name" },
            setNavigationState = {},
            navigationState = GalleryNavState.STOP,
            pauseTimer = {},
            startTimer = {},
            autoplay = AutoplayCommons()
        )
    }
}