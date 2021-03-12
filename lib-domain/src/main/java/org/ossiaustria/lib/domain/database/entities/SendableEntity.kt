package org.ossiaustria.lib.domain.database.entities

import java.util.*

interface SendableEntity : AbstractEntity {
    val id: UUID
    val createdAt: Long
    val sendAt: Long
    val retrievedAt: Long
    val senderId: UUID
    val receiverId: UUID
}


