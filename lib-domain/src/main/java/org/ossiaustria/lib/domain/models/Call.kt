package org.ossiaustria.lib.domain.models

import org.ossiaustria.lib.domain.models.enums.CallType
import java.util.*

data class Call(
    override val id: UUID,
    override val createdAt: Long = System.currentTimeMillis(),
    override val sendAt: Long? = null,
    override val retrievedAt: Long? = null,
    override val senderId: UUID,
    override val receiverId: UUID,

    val callType: CallType,
    val startedAt: Long,
    val finishedAt: Long,
) : Sendable