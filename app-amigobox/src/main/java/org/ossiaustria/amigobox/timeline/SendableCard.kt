import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.ossiaustria.amigobox.timeline.content.AlbumShareContent
import org.ossiaustria.amigobox.timeline.content.CallContent
import org.ossiaustria.amigobox.timeline.content.MessageContent
import org.ossiaustria.amigobox.timeline.content.MissedCallContent
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.AlbumShare
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.Message
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.Sendable
import org.ossiaustria.lib.domain.models.enums.CallState
import java.util.*
import kotlin.time.ExperimentalTime

@ExperimentalTime
@Composable
fun SendableCard(
    sendable: Sendable,
    toAlbum: (Album) -> Unit,
    centerPerson: Person,
    toCall: (Person) -> Unit,
    findPerson: (UUID) -> Person?,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        backgroundColor = Color.Transparent,
        elevation = UIConstants.ScrollableCardList.CARD_ELEVATION
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.padding(top = UIConstants.TimelineFragment.SENDABLE_CARD_SPACER_PADDING))
            when (sendable) {
                is AlbumShare -> AlbumShareContent(sendable, toAlbum)
                is Call -> {
                    if ((sendable.callState == CallState.TIMEOUT) && (sendable.receiverId == centerPerson.id)) {
                        MissedCallContent(
                            sendable,
                            centerPerson,
                            findPerson,
                            toCall
                        )
                    } else {
                        CallContent(
                            sendable,
                            centerPerson,
                            findPerson,
                            toCall
                        )
                    }
                }
                is Message -> MessageContent(sendable, findPerson)
            }
        }
    }
}

