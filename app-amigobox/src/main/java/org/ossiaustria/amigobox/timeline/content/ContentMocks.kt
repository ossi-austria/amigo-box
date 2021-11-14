package org.ossiaustria.amigobox.timeline.content

import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.AlbumShare
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.Group
import org.ossiaustria.lib.domain.models.Multimedia
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.CallState
import org.ossiaustria.lib.domain.models.enums.CallType
import org.ossiaustria.lib.domain.models.enums.MemberType
import org.ossiaustria.lib.domain.models.enums.MultimediaType
import java.util.*
import java.util.UUID.randomUUID

object ContentMocks {

    private val groupId = randomUUID()
    val centerPerson = Person(
        randomUUID(),
        name = "CenterPerson",
        groupId = groupId,
        memberType = MemberType.ANALOGUE,
        avatarUrl = "https://thispersondoesnotexist.com/image"
    )
    val otherPerson = Person(
        randomUUID(),
        name = "OtherPerson",
        groupId = groupId,
        memberType = MemberType.ADMIN,
        avatarUrl = "https://thispersondoesnotexist.com/image"
    )
    private val group = Group(
        groupId, "Group", members = listOf(
            centerPerson, otherPerson
        )
    )
    val call = Call(
        id = randomUUID(),
        senderId = otherPerson.id,
        receiverId = centerPerson.id,
        callType = CallType.VIDEO,
        callState = CallState.ACCEPTED,
        startedAt = Date()
    )

    private val albumId = randomUUID()
    val album = Album(
        id = albumId,
        name = "Album",
        items = listOf(
            Multimedia(
                randomUUID(),
                otherPerson.id,
                filename = "Filename",
                contentType = "JPG",
                type = MultimediaType.VIDEO,
                albumId = albumId
            )
        ),
        ownerId = otherPerson.id,
    )

    val albumShare = AlbumShare(
        randomUUID(),
        senderId = otherPerson.id,
        receiverId = centerPerson.id,
        album = album
    )
}