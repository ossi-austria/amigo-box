package org.ossiaustria.lib.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.MembershipType
import java.util.*

@Entity(tableName = "persons")
data class PersonEntity(
    @PrimaryKey
    val personId: UUID,

    val name: String,
    val email: String?,

    val groupId: UUID?,
    val memberType: MembershipType,
    val avatarUrl: String? = null

) : AbstractEntity

internal fun PersonEntity.toPerson(): Person {

    return Person(
        id = this.personId,
        email = this.email,
        name = this.name,
        memberType = this.memberType,
        groupId = this.groupId,
    )
}

internal fun Person.toPersonEntity(): PersonEntity {

    return PersonEntity(
        personId = this.id,
        email = this.email,
        name = this.name,
        memberType = this.memberType,
        groupId = this.groupId,
    )
}

