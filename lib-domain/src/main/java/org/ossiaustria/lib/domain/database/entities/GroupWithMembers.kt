package org.ossiaustria.lib.domain.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import org.ossiaustria.lib.domain.models.Group
import org.ossiaustria.lib.domain.models.enums.MembershipType

internal data class GroupWithMembers(
    @Embedded val group: GroupEntity,
    @Relation(
        entity = PersonEntity::class,
        parentColumn = "groupId",
        entityColumn = "groupId",
    )
    val members: List<PersonEntity>
)

internal fun GroupWithMembers.toGroup(): Group {

    val members = this.members
    return Group(
        id = this.group.groupId,
        name = this.group.name,
        members = members.map { it.toPerson() },
        centerPerson = members.filter { it.memberType == MembershipType.CENTER }
            .map { it.toPerson() }
            .firstOrNull(),
        admins = members.filter { it.memberType == MembershipType.ADMIN }.map { it.toPerson() }
    )
}


