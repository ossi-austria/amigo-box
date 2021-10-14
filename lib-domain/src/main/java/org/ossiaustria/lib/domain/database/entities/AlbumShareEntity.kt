package org.ossiaustria.lib.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "album_shares")
data class AlbumShareEntity(
    @PrimaryKey
    override val id: UUID,
    override val createdAt: Date = Date(),
    override val sendAt: Date? = null,
    override val retrievedAt: Date? = null,
    override val senderId: UUID,
    override val receiverId: UUID,

    val albumId: UUID
) : SendableEntity