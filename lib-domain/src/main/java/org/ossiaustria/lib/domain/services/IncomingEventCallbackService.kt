package org.ossiaustria.lib.domain.services

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.modules.UserContext
import org.ossiaustria.lib.domain.repositories.CallRepository
import timber.log.Timber
import java.util.*

interface IncomingEventCallbackService {
    fun informWillJoin()
    fun informTerminated()
    fun informJoined()
    fun informParticipantJoined()
    fun informParticipantLeft()
    fun handleEvent(cloudEvent: AmigoCloudEvent): Boolean
    fun observe(function: IncomingEventCallback)
    fun stopObserving()
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
) {

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

interface IncomingEventCallback {
    fun onSuccess(call: Call)
    fun onError(e: Throwable?)
}

class IncomingEventCallbackServiceImpl(
    ioDispatcher: CoroutineDispatcher,
    private val userContext: UserContext,
    private val callRepository: CallRepository
) : IncomingEventCallbackService {

    private val scope = CoroutineScope(ioDispatcher)
    var incomingEventCallback: IncomingEventCallback? = null

    override fun informWillJoin() {
        Timber.i("informWillJoin")
    }

    override fun informTerminated() {
        Timber.i("informTerminated")
    }

    override fun informJoined() {
        Timber.i("informJoined")
    }

    override fun informParticipantJoined() {
        Timber.i("informParticipantJoined")
    }

    override fun informParticipantLeft() {
        Timber.i("informParticipantLeft")
    }

    override fun observe(function: IncomingEventCallback) {
        incomingEventCallback = function
    }

    override fun stopObserving() {
        incomingEventCallback = null
    }

    override fun handleEvent(cloudEvent: AmigoCloudEvent): Boolean {
        return if (cloudEvent.type == AmigoCloudEventType.CALL && incomingEventCallback != null) {
            scope.launch {
                callRepository.getCall(cloudEvent.entityId, true).collect {
                    if (it is Resource.Success) {
                        incomingEventCallback?.onSuccess(it.value)
                    } else if (it is Resource.Failure) {
                        Timber.e(it.throwable, "Error during handling $cloudEvent")
                        incomingEventCallback?.onError(it.throwableOrNull())
                    }
                }
            }
            true
        } else {
            false
        }
    }

}