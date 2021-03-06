package org.ossiaustria.lib.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "albums")
data class AlbumEntity(
    @PrimaryKey
    val albumId: UUID,
    val ownerId: UUID,
    val name: String,
    val createdAt: Date = Date(),
    val updatedAt: Date? = null,
) : AbstractEntity


