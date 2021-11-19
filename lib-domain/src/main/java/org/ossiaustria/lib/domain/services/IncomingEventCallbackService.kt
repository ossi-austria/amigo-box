package org.ossiaustria.lib.domain.services

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.repositories.CallRepository
import timber.log.Timber
import java.io.Serializable
import java.util.*

interface IncomingEventCallbackService {
    fun informWillJoin()
    fun informTerminated()
    fun informJoined()
    fun informParticipantJoined()
    fun informParticipantLeft()
    fun observe(function: IncomingEventCallback)
    fun stopObserving()
    fun handleCloudEventCall(cloudEvent: AmigoCloudEvent, fallbackAction: (Call) -> Unit)
}

enum class AmigoCloudEventType {
    CALL,
    MESSAGE,
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

enum class CallEvent {
    PARTICIPANT_JOINED,
    FINISHED,
}

interface IncomingEventCallback {
    fun onSuccess(call: Call): Boolean
    fun onJitsiCallEvent(callEvent: CallEvent): Boolean
}

class IncomingEventCallbackServiceImpl(
    ioDispatcher: CoroutineDispatcher,
    private val callRepository: CallRepository
) : IncomingEventCallbackService {

    private val scope = CoroutineScope(ioDispatcher)
    var incomingEventCallback: IncomingEventCallback? = null

    override fun informWillJoin() {
        Timber.i("informWillJoin")
    }

    override fun informTerminated() {
        Timber.i("informTerminated")
        incomingEventCallback?.onJitsiCallEvent(CallEvent.FINISHED)
    }

    override fun informJoined() {
        Timber.i("informJoined")
        incomingEventCallback?.onJitsiCallEvent(CallEvent.PARTICIPANT_JOINED)
    }

    override fun informParticipantJoined() {
        Timber.i("informParticipantJoined")
        incomingEventCallback?.onJitsiCallEvent(CallEvent.PARTICIPANT_JOINED)
    }

    override fun informParticipantLeft() {
        Timber.i("informParticipantLeft")
        incomingEventCallback?.onJitsiCallEvent(CallEvent.FINISHED)
    }

    override fun observe(function: IncomingEventCallback) {
        incomingEventCallback = function
    }

    override fun stopObserving() {
        incomingEventCallback = null
    }

    override fun handleCloudEventCall(cloudEvent: AmigoCloudEvent, fallbackAction: (Call) -> Unit) {
        Timber.i("Handle cloudEvent: $cloudEvent")
        Timber.i("Handle cloudEvent: ${cloudEvent.type}, incomingEventCallback != null: ${incomingEventCallback != null}")

        val callResource = runBlocking(scope.coroutineContext) {
            callRepository.getCall(cloudEvent.entityId, true).first { it !is Resource.Loading }
        }
        when (callResource) {
            is Resource.Success -> {
                val call = callResource.value
                Timber.i("Handle cloudEvent: Resource.Success: $call")
                val completedByObservers = incomingEventCallback?.onSuccess(call) ?: false
                if (!completedByObservers) {
                    fallbackAction(call)
                }
            }
            is Resource.Failure -> {
                Timber.w("Handle cloudEvent: Cannot retrieve call of ")
                Timber.e(callResource.throwable, "Error during handling $cloudEvent")
            }
        }
    }

}