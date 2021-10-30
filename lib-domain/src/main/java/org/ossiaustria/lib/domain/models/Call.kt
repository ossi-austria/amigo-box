package org.ossiaustria.lib.domain.models

import org.ossiaustria.lib.domain.models.enums.CallState
import org.ossiaustria.lib.domain.models.enums.CallType
import java.io.Serializable
import java.util.*

data class Call(
    override val id: UUID,
    override val createdAt: Date = Date(),
    override val sentAt: Date? = null,
    override val retrievedAt: Date? = null,
    override val senderId: UUID,
    override val receiverId: UUID,

    val callType: CallType,
    val callState: CallState,
    val token: String? = null,
    val startedAt: Date? = null,
    val finishedAt: Date? = null,
) : Sendable, Serializable {

    val duration: Long? = if (startedAt != null) {
        val endTime = finishedAt ?: Date()
        endTime.time - startedAt.time
    } else null
}