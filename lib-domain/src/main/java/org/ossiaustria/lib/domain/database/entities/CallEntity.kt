package org.ossiaustria.lib.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.enums.CallType
import java.util.*

@Entity(tableName = "calls")
data class CallEntity(
    @PrimaryKey
    override val id: UUID,
    override val createdAt: Long,
    override val sendAt: Long,
    override val retrievedAt: Long,
    override val senderId: UUID,
    override val receiverId: UUID,

    val callType: CallType,
    val startedAt: Long,
    val finishedAt: Long,
) : SendableEntity

fun CallEntity.toCall(): Call {

    return Call(
        id = this.id,
        createdAt = this.createdAt,
        sendAt = this.sendAt,
        retrievedAt = this.retrievedAt,
        senderId = this.senderId,
        receiverId = this.receiverId,

        callType = this.callType,
        startedAt = this.startedAt,
        finishedAt = this.finishedAt,
    )
}