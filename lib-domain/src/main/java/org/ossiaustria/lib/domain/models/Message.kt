package org.ossiaustria.lib.domain.models

import java.util.*

data class Message(
    override val id: UUID,
    override val createdAt: Date = Date(),
    override val sentAt: Date? = null,
    override val retrievedAt: Date? = null,
    override val senderId: UUID,
    override val receiverId: UUID,

    val text: String,
    val multimedia: Multimedia? = null,
    val multimediaId: UUID? = null,
) : Sendable