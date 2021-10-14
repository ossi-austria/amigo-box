package org.ossiaustria.lib.domain.models

import java.util.*

data class AlbumShare(
    override val id: UUID,
    override val createdAt: Date = Date(),
    override val sendAt: Date? = null,
    override val retrievedAt: Date? = null,
    override val senderId: UUID,
    override val receiverId: UUID,

    val album: Album
) : Sendable