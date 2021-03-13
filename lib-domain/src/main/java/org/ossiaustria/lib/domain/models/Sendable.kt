package org.ossiaustria.lib.domain.models

import org.ossiaustria.lib.domain.models.enums.CallType
import org.ossiaustria.lib.domain.models.enums.MultimediaType
import java.util.*

interface Sendable {
    val id: UUID
    val createdAt: Long
    val sendAt: Long
    val retrievedAt: Long
    val senderId: UUID
    val receiverId: UUID
}

data class AlbumShare(
    override val id: UUID,
    override val createdAt: Long,
    override val sendAt: Long,
    override val retrievedAt: Long,
    override val senderId: UUID,
    override val receiverId: UUID,

    val album: Album
) : Sendable


data class Multimedia(
    override val id: UUID,
    override val createdAt: Long,
    override val sendAt: Long,
    override val retrievedAt: Long,
    override val senderId: UUID,
    override val receiverId: UUID,

    val ownerId: UUID,
    val remoteUrl: String,
    val localUrl: String,
    val type: MultimediaType,
    val size: Long? = null,
    val albumId: UUID? = null
) : Sendable

data class Message(
    override val id: UUID,
    override val createdAt: Long,
    override val sendAt: Long,
    override val retrievedAt: Long,
    override val senderId: UUID,
    override val receiverId: UUID,

    val text: String
) : Sendable

data class Call(
    override val id: UUID,
    override val createdAt: Long,
    override val sendAt: Long,
    override val retrievedAt: Long,
    override val senderId: UUID,
    override val receiverId: UUID,

    val callType: CallType,
    val startedAt: Long,
    val finishedAt: Long,
) : Sendable