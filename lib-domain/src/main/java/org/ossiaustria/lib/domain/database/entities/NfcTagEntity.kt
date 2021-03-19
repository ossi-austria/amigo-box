package org.ossiaustria.lib.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.ossiaustria.lib.domain.models.NfcTag
import org.ossiaustria.lib.domain.models.enums.NfcTagType
import java.util.*

@Entity(tableName = "nfcs")
data class NfcTagEntity(
    @PrimaryKey
    val nfcTagId: UUID,
    val creatorId: UUID,
    val createdAt: Long = System.currentTimeMillis(),
    val ownerId: UUID? = null,
    val type: NfcTagType,
    val linkedPersonId: UUID? = null,
    val linkedMediaId: UUID? = null,
    val linkedAlbumId: UUID? = null
) : AbstractEntity

internal fun NfcTagEntity.toNfcTag() = NfcTag(
    id = this.nfcTagId,
    creatorId = this.creatorId,
    ownerId = this.ownerId,
    type = this.type,
    linkedPersonId = this.linkedPersonId,
    linkedMediaId = this.linkedMediaId,
    linkedAlbumId = this.linkedAlbumId,
)


internal fun NfcTag.toNfcTagEntity() = NfcTagEntity(
    nfcTagId = this.id,
    creatorId = this.creatorId,
    createdAt = this.createdAt,
    ownerId = this.ownerId,
    type = this.type,
    linkedPersonId = this.linkedPersonId,
    linkedMediaId = this.linkedMediaId,
    linkedAlbumId = this.linkedAlbumId,
)

