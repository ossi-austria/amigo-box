package org.ossiaustria.lib.domain.models

import java.util.*

interface Sendable {
    val id: UUID
    val createdAt: Date
    val sentAt: Date?
    val retrievedAt: Date?
    val senderId: UUID
    val receiverId: UUID
}
