package org.ossiaustria.amigobox.ui.timeline.content

import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.MemberType
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

}