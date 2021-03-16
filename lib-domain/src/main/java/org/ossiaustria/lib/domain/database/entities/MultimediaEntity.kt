package org.ossiaustria.lib.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.ossiaustria.lib.domain.models.Multimedia
import org.ossiaustria.lib.domain.models.enums.MultimediaType
import java.util.*

@Entity(tableName = "multimedias")
data class MultimediaEntity(
    @PrimaryKey
    override val id: UUID,
    override val createdAt: Long,
    override val sendAt: Long? = null,
    override val retrievedAt: Long? = null,
    override val senderId: UUID,
    override val receiverId: UUID,

    val ownerId: UUID,
    val remoteUrl: String,
    val localUrl: String,
    val type: MultimediaType,
    val size: Long? = null,
    val albumId: UUID? = null
) : SendableEntity

internal fun MultimediaEntity.toMultimedia(): Multimedia {

    return Multimedia(
        id = this.id,
        createdAt = this.createdAt,
        sendAt = this.sendAt,
        retrievedAt = this.retrievedAt,
        senderId = this.senderId,
        receiverId = this.receiverId,

        ownerId = this.ownerId,
        remoteUrl = this.remoteUrl,
        localUrl = this.localUrl,
        type = this.type,
        size = this.size,
        albumId = this.albumId,
    )
}

internal fun Multimedia.toMultimediaEntity(): MultimediaEntity {

    return MultimediaEntity(
        id = this.id,
        createdAt = this.createdAt,
        sendAt = this.sendAt,
        retrievedAt = this.retrievedAt,
        senderId = this.senderId,
        receiverId = this.receiverId,

        ownerId = this.ownerId,
        remoteUrl = this.remoteUrl,
        localUrl = this.localUrl,
        type = this.type,
        size = this.size,
        albumId = this.albumId,
    )
}