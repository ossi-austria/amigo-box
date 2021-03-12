package org.ossiaustria.lib.domain.database.entities

import androidx.room.Embedded
import androidx.room.Relation

//internal data class MembershipsWithPerson(
//    @Embedded val membershipEntity: MembershipEntity,
//    @Relation(
//        parentColumn = "membershipId",
//        entityColumn = "personId",
//    )
//    val personEntity: PersonEntity
//)

internal data class PersonWithMembership(
    @Embedded val personEntity: PersonEntity,
    @Relation(
        parentColumn = "personId",
        entityColumn = "memberId",
    )
    val membershipEntity: MembershipEntity
)

