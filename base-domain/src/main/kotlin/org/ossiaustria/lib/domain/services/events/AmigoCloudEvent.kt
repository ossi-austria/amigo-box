package org.ossiaustria.lib.domain.services.events

import timber.log.Timber
import java.io.Serializable
import java.util.*

enum class AmigoCloudEventType {
    CALL,
    MESSAGE,
}

enum class CallEvent {
    FINISHED,
}

data class AmigoCloudEvent(
    val type: AmigoCloudEventType,
    val entityId: UUID,
    val receiverId: UUID,
    val action: String,
) : Serializable {

    companion object {
        fun fromMap(map: Map<String, String>): AmigoCloudEvent? = try {
            AmigoCloudEvent(
                type = AmigoCloudEventType.valueOf(map["type"]!!.uppercase()),
                entityId = UUID.fromString(map["entity_id"]!!),
                receiverId = UUID.fromString(map["receiver_id"]!!),
                action = map["action"]!!
            )
        } catch (e: Exception) {
            Timber.e(e, "Cloud message cannot be parsed")
            null
        }
    }
}