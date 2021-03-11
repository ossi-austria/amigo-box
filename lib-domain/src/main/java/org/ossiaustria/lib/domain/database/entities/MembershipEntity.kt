package org.ossiaustria.lib.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "memberships")
internal data class MembershipEntity(
    @PrimaryKey
    val membershipId: UUID,

    val groupId: UUID,
    val type: MembershipType
)

@Entity(
    tableName = "membership_person_refs",
    primaryKeys = ["membershipId", "personId", ]
)
internal data class MembershipPersonRef(
    val membershipId: UUID,
    val personId: UUID
)