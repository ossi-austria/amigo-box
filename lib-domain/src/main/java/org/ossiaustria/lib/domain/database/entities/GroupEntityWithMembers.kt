package org.ossiaustria.lib.domain.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import org.ossiaustria.lib.domain.models.Group
import org.ossiaustria.lib.domain.models.enums.MembershipType

data class GroupEntityWithMembers(

    @Embedded
    val group: GroupEntity,

    @Relation(
        entity = PersonEntity::class,
        parentColumn = "groupId",
        entityColumn = "groupId",
    )
    val members: List<PersonEntity>
)

 fun GroupEntityWithMembers.toGroup(): Group {

     val members = this.members
     return Group(
         id = this.group.groupId,
         name = this.group.name,
         members = members.map { it.toPerson() },
         centerPerson = members.filter { it.memberType == MembershipType.CENTER }
             .map(PersonEntity::toPerson)
             .firstOrNull(),
         admins = members.filter { it.memberType == MembershipType.ADMIN }
             .map(PersonEntity::toPerson)
     )
 }


