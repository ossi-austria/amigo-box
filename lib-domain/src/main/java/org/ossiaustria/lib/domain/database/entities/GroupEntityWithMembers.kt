package org.ossiaustria.lib.domain.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import org.ossiaustria.lib.domain.models.Group
import org.ossiaustria.lib.domain.models.Person

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
    return Group(
        id = this.group.groupId,
        name = this.group.name,
        members = this.members.map(PersonEntity::toPerson),
    )
}

fun List<GroupEntityWithMembers>.toGroupList(): List<Group> {
    return this.map { it.toGroup() }
}

fun Group.toGroupEntity() = GroupEntity(
    groupId = this.id,
    name = this.name,
)

fun Group.toPersonEntityList(): List<PersonEntity> =
    this.members.map(Person::toPersonEntity)
