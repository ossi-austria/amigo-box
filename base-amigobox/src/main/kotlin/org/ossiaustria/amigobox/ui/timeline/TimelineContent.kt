package org.ossiaustria.amigobox.ui.timeline

import SendableContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.autoplay.TimerNavigationButtonsRow
import org.ossiaustria.amigobox.ui.autoplay.TimerState
import org.ossiaustria.amigobox.ui.commons.DefaultHelpButton
import org.ossiaustria.amigobox.ui.commons.TextAndIconButton
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.AlbumShare
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.Message
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.Sendable
import java.util.*

@Composable
fun TimelineContent(
    currentIndex: Int,
    timerState: TimerState,
    centerPerson: Person,
    sendables: List<Sendable>,
    findPerson: (UUID) -> Person?,
    toHome: () -> Unit,
    toAlbum: (Album) -> Unit,
    toCall: (Person) -> Unit,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onStartStopPressed: () -> Unit,
    handleMessage: (Message) -> Unit

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        // top bar
        Row(modifier = Modifier.fillMaxWidth()) {
            TimelineContentHeader(
                modifier = Modifier.weight(1F),
                currentIndex = currentIndex,
                centerPerson = centerPerson,
                sendables = sendables,
                findPerson = findPerson
            )
            TextAndIconButton(
                iconId = R.drawable.ic_home_icon,
                text = stringResource(id = R.string.back_home_description),
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                onClick = toHome
            )
            DefaultHelpButton()
        }
        // center main content
        val listState = rememberLazyListState()
        LazyRow(
            modifier = Modifier
                .weight(1F)
                .fillMaxWidth(),
            state = listState
        ) {
            items(items = sendables, itemContent = { sendable ->
                Box(
                    modifier = Modifier.fillParentMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    SendableContent(
                        sendable = sendable,
                        centerPerson = centerPerson,
                        toAlbum = toAlbum,
                        toCall = toCall,
                        findPerson = findPerson,
                        handleMessage = handleMessage
                    )
                }
            })
        }
        LaunchedEffect(currentIndex) {
            listState.animateScrollToItem(currentIndex)
        }
    }
    // bottom bar
    TimerNavigationButtonsRow(
        currentIndex = currentIndex,
        timerState = timerState,
        onPreviousPressed = onPreviousPressed,
        onNextPressed = onNextPressed,
        onStartStopPressed = onStartStopPressed,
    )
}

@Composable
fun stringForSendable(sendable: Sendable, personName: String) = when (sendable) {
    is AlbumShare -> stringResource(R.string.album_by, personName)
    is Call -> stringResource(R.string.call_by, personName)
    is Message -> stringResource(R.string.message_by, personName)
    else -> ""
}