package org.ossiaustria.lib.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.ossiaustria.lib.domain.models.Person
import java.util.*

@Entity(tableName = "members")
internal data class MemberEntity(
    @PrimaryKey
    val memberId: UUID,

    val name: String,
    val email: String,

    val groupId: UUID,
    val memberType: MembershipType
)

internal fun MemberEntity.toPerson(): Person {

    return Person(
        id = this.memberId,
        email = this.email,
        name = this.name,
        memberType = this.memberType.toEnum()
    )
}

