package org.ossiaustria.lib.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.ossiaustria.lib.domain.models.AmigoNfcInfo
import org.ossiaustria.lib.domain.models.enums.NfcTagType
import java.util.*

@Entity(tableName = "nfcs")
data class NfcInfoEntity(
    @PrimaryKey
    val nfcTagId: UUID,
    val ownerId: UUID? = null,
    val creatorId: UUID,
    val type: NfcTagType,
    val name: String,
    val nfcRef: String,
    val linkedPersonId: UUID? = null,
    val linkedAlbumId: UUID? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date? = null,
) : AbstractEntity

internal fun NfcInfoEntity.toNfcTag() = AmigoNfcInfo(
    id = this.nfcTagId,
    creatorId = this.creatorId,
    ownerId = this.ownerId,
    type = this.type,
    name = this.name,
    nfcRef = this.nfcRef,
    linkedPersonId = this.linkedPersonId,
    linkedAlbumId = this.linkedAlbumId,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)


internal fun AmigoNfcInfo.toNfcTagEntity() = NfcInfoEntity(
    nfcTagId = this.id,
    creatorId = this.creatorId,
    ownerId = this.ownerId,
    type = this.type,
    name = this.name,
    nfcRef = this.nfcRef,
    linkedPersonId = this.linkedPersonId,
    linkedAlbumId = this.linkedAlbumId,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)

