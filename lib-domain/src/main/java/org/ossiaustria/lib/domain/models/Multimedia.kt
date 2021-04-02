package org.ossiaustria.lib.domain.models

import org.ossiaustria.lib.domain.models.enums.MultimediaType
import java.util.*

data class Multimedia(

    override val id: UUID,
    override val createdAt: Long = System.currentTimeMillis(),
    override val sendAt: Long? = null,
    override val retrievedAt: Long? = null,
    override val senderId: UUID,
    override val receiverId: UUID,

    val ownerId: UUID,
    val remoteUrl: String,
    val localUrl: String,
    val type: MultimediaType,
    val size: Long? = null,
    val albumId: UUID? = null,

    ) : Sendable