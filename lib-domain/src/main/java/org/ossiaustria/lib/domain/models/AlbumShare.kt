package org.ossiaustria.lib.domain.models

import java.util.*

data class AlbumShare(
    override val id: UUID,
    override val createdAt: Long = System.currentTimeMillis(),
    override val sendAt: Long? = null,
    override val retrievedAt: Long? = null,
    override val senderId: UUID,
    override val receiverId: UUID,

    val album: Album
) : Sendable