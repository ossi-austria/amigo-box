package org.ossiaustria.lib.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "album_shares")
data class AlbumShareEntity(
    @PrimaryKey
    override val id: UUID,
    override val createdAt: Long = System.currentTimeMillis(),
    override val sendAt: Long? = null,
    override val retrievedAt: Long? = null,
    override val senderId: UUID,
    override val receiverId: UUID,

    val albumId: UUID
) : SendableEntity