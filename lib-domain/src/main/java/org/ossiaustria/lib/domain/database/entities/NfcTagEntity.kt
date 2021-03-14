package org.ossiaustria.lib.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.ossiaustria.lib.domain.models.enums.NfcTagType
import java.util.*

@Entity(tableName = "albums")
data class NfcTagEntity(
    @PrimaryKey
    val nfcTagId: UUID,
    val creatorId: UUID,
    val ownerId: UUID,
    val type: NfcTagType,
    val linkedPersonId: UUID? = null,
    val linkedMediaId: UUID? = null,
    val linkedAlbumId: UUID? = null
) : AbstractEntity

