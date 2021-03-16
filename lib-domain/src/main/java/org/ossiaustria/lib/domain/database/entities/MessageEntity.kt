package org.ossiaustria.lib.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.ossiaustria.lib.domain.models.Message
import java.util.*

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    override val id: UUID,
    override val createdAt: Long,
    override val sendAt: Long? = null,
    override val retrievedAt: Long? = null,
    override val senderId: UUID,
    override val receiverId: UUID,

    val text: String,
) : SendableEntity

internal fun MessageEntity.toMessage(): Message {

    return Message(
        id = this.id,
        createdAt = this.createdAt,
        sendAt = this.sendAt,
        retrievedAt = this.retrievedAt,
        senderId = this.senderId,
        receiverId = this.receiverId,
        text = this.text
    )
}