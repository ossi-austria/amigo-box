package org.ossiaustria.lib.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey
    val groupId: UUID,
    val name: String,
) : AbstractEntity

