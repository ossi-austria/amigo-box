package org.ossiaustria.amigobox

import org.ossiaustria.lib.domain.auth.Account
import org.ossiaustria.lib.domain.auth.TokenResult
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.MemberType
import java.util.*
import java.util.UUID.randomUUID

object EntityMocks {

    fun account(id: UUID = randomUUID(), email: String = "test@example.org"): Account {
        return Account(
            id = id,
            email = email
        )
    }

    fun tokenResult(token: String = "token", subject: String = "subject"): TokenResult {
        return TokenResult(
            token = token,
            subject = subject,
            issuedAt = Date(),
            expiration = Date(),
            issuer = "test",
        )
    }

    fun person(
        personId: UUID,
        groupId: UUID,
        memberType: MemberType = MemberType.MEMBER
    ): Person {
        return Person(
            id = personId,
            name = "name",
            email = "email",
            memberType = memberType,
            groupId = groupId
        )
    }

    fun album(
        id: UUID = randomUUID(),
        ownerId: UUID = randomUUID(),
        name: String = "name"
    ): Album = Album(
        id = id,
        name = name,
        ownerId = ownerId,
        items = listOf()
    )
}

