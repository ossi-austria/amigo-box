package org.ossiaustria.lib.domain.models

import org.ossiaustria.lib.domain.models.enums.NfcTagType
import java.time.Instant
import java.util.*

data class AmigoNfcInfo(
    val id: UUID,
    val ownerId: UUID? = null,
    val creatorId: UUID,
    val type: NfcTagType,
    val name: String,
    val nfcRef: String,
    val linkedPersonId: UUID? = null,
    val linkedAlbumId: UUID? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date? = null,
)