package org.ossiaustria.lib.domain.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import org.ossiaustria.lib.domain.database.entities.MembershipType.ADMIN
import org.ossiaustria.lib.domain.models.Group

internal data class GroupWithMembershipsAndPersons(
    @Embedded val group: GroupEntity,
    @Relation(
        entity = MembershipEntity::class,
        parentColumn = "groupId",
        entityColumn = "membershipId",
    )
    val members: List<MembershipsWithPerson>
)

internal fun GroupWithMembershipsAndPersons.toGroup(): Group {

    val members = this.members
    return Group(
        id = this.group.groupId,
        name = this.group.name,
        members = members.map { it.personEntity.toPerson() },
        centerPerson = members.filter { it.membershipEntity.type == ADMIN }
            .map { it.personEntity.toPerson() }.firstOrNull(),
        admins = members.filter { it.membershipEntity.type == ADMIN }
            .map { it.personEntity.toPerson() }
    )
}


