package org.ossiaustria.lib.domain.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import org.ossiaustria.lib.domain.database.entities.MembershipType.ADMIN
import org.ossiaustria.lib.domain.database.entities.MembershipType.CENTER
import org.ossiaustria.lib.domain.models.Group

internal data class GroupWithMembers(
    @Embedded val group: GroupEntity,
    @Relation(
        entity = MemberEntity::class,
        parentColumn = "groupId",
        entityColumn = "groupId",
    )
    val members: List<MemberEntity>
)

internal fun GroupWithMembers.toGroup(): Group {

    val members = this.members
    return Group(
        id = this.group.groupId,
        name = this.group.name,
        members = members.map { it.toPerson() },
        centerPerson = members.filter { it.memberType == CENTER }.map { it.toPerson() }
            .firstOrNull(),
        admins = members.filter { it.memberType == ADMIN }.map { it.toPerson() }
    )
}


