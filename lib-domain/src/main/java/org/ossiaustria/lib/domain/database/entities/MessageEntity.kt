package org.ossiaustria.lib.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    override val id: UUID,
    override val createdAt: Long,
    override val sendAt: Long,
    override val retrievedAt: Long,
    override val senderId: UUID,
    override val receiverId: UUID,

    val text: String,
) : SendableEntity