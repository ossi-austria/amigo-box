package org.ossiaustria.lib.domain.models

import org.ossiaustria.lib.domain.models.enums.CallType
import java.util.*

data class Call(
    override val id: UUID,
    override val createdAt: Date = Date(),
    override val sendAt: Date? = null,
    override val retrievedAt: Date? = null,
    override val senderId: UUID,
    override val receiverId: UUID,

    val callType: CallType,
    val startedAt: Date?,
    val finishedAt: Date?,
) : Sendable