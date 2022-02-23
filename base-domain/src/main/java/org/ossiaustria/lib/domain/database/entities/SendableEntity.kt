package org.ossiaustria.lib.domain.database.entities

import java.util.*

interface SendableEntity : AbstractEntity {
    val id: UUID
    val createdAt: Date
    val sendAt: Date?
    val retrievedAt: Date?
    val senderId: UUID
    val receiverId: UUID
}


