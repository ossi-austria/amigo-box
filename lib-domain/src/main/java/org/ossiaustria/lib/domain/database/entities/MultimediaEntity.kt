package org.ossiaustria.lib.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.ossiaustria.lib.domain.models.Multimedia
import org.ossiaustria.lib.domain.models.enums.MultimediaType
import java.util.*

@Entity(tableName = "multimedias")
data class MultimediaEntity(
    @PrimaryKey
    val id: UUID,
    val createdAt: Date = Date(),
    val ownerId: UUID,
    val filename: String,
    val contentType: String,
    val type: MultimediaType,
    val size: Long? = null,
    val albumId: UUID? = null
) : AbstractEntity

internal fun MultimediaEntity.toMultimedia(): Multimedia {

    return Multimedia(
        id = this.id,
        createdAt = this.createdAt,
        ownerId = this.ownerId,
        filename = this.filename,
        contentType = this.contentType,
        type = this.type,
        size = this.size,
        albumId = this.albumId,
    )
}

internal fun Multimedia.toMultimediaEntity(): MultimediaEntity {

    return MultimediaEntity(
        id = this.id,
        createdAt = this.createdAt,
        ownerId = this.ownerId,
        filename = this.filename,
        contentType = this.contentType,
        type = this.type,
        size = this.size,
        albumId = this.albumId,
    )
}