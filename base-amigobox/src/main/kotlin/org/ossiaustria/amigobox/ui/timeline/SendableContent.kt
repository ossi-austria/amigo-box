import androidx.compose.runtime.Composable
import org.ossiaustria.amigobox.ui.timeline.content.AlbumShareContent
import org.ossiaustria.amigobox.ui.timeline.content.CallContent
import org.ossiaustria.amigobox.ui.timeline.content.MessageContent
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.AlbumShare
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.Message
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.Sendable
import java.util.*

@Composable
fun SendableContent(
    sendable: Sendable,
    centerPerson: Person,
    toAlbum: (Album) -> Unit,
    toCall: (Person) -> Unit,
    findPerson: (UUID) -> Person?,
    handleMessage: (Message) -> Unit

) {
    when (sendable) {
        is AlbumShare -> AlbumShareContent(sendable, toAlbum)
        is Call -> {
            CallContent(
                sendable,
                centerPerson,
                findPerson,
                toCall
            )
        }
        is Message -> MessageContent(sendable, findPerson, handleMessage)
    }
}

