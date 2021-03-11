package org.ossiaustria.lib.domain.database.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

internal data class MembershipsWithPerson(
    @Embedded val membershipEntity: MembershipEntity,
    @Relation(
        parentColumn = "membershipId",
        entityColumn = "personId",
        associateBy = Junction(MembershipPersonRef::class)
    )
    val personEntity: PersonEntity
)

