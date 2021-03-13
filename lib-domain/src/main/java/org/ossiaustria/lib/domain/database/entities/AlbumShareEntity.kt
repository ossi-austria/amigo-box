package org.ossiaustria.lib.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "album_shares")
internal data class AlbumShareEntity(
    @PrimaryKey
    override val id: UUID,
    override val createdAt: Long = System.currentTimeMillis(),
    override val sendAt: Long = System.currentTimeMillis(),
    override val retrievedAt: Long = System.currentTimeMillis(),
    override val senderId: UUID,
    override val receiverId: UUID,

    val albumId: UUID
) : SendableEntity