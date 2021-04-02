package org.ossiaustria.lib.domain.models

import java.util.*

interface Sendable {
    val id: UUID
    val createdAt: Long
    val sendAt: Long?
    val retrievedAt: Long?
    val senderId: UUID
    val receiverId: UUID
}
