package org.ossiaustria.lib.domain.models

import org.ossiaustria.lib.domain.models.enums.NfcTagType
import java.util.*


data class NfcTag(
    val id: UUID,
    val creatorId: UUID,
    val createdAt: Long = System.currentTimeMillis(),
    val ownerId: UUID? = null,
    val type: NfcTagType,
    val linkedPersonId: UUID? = null,
    val linkedMediaId: UUID? = null,
    val linkedAlbumId: UUID? = null,
)