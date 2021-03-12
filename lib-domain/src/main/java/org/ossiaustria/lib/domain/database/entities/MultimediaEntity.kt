package org.ossiaustria.lib.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.ossiaustria.lib.domain.models.enums.MultimediaType
import java.util.*

@Entity(tableName = "multimedias")
data class MultimediaEntity(
    @PrimaryKey
    override val id: UUID,
    override val createdAt: Long,
    override val sendAt: Long,
    override val retrievedAt: Long,
    override val senderId: UUID,
    override val receiverId: UUID,

    val ownerId: UUID,
    val remoteUrl: String,
    val localUrl: String,
    val type: MultimediaType,
    val size: Long? = null,
    val collectionId: UUID? = null
) : SendableEntity