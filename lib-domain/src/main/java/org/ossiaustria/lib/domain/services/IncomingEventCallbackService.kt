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

/**
 * Handles all FCM CloudEvents and local Jitsi Broadcast events
 */
interface IncomingEventCallbackService {

    fun informWillJoin()

    /**
     * Caller or Callee terminated JitsiCall
     */
    fun informTerminated()
    fun informJoined()
    fun informParticipantJoined()

    /**
     * Caller or Callee left JitsiCall
     */
    fun informParticipantLeft()
    fun observe(function: IncomingEventCallback)
    fun stopObserving()

    /**
     * This method performs the necessary checks to know, whether we can run in foreground (have connected ViewModels and UI)
     * or we start from background or even a killed state.
     *
     * With *fallbackAction* it is asserted, that any time something is executed.
     * Either
     *   a) A IncomingEventsViewModel is connected and has Observers -> notifiedCall will be published
     *   b) IncomingEventsViewModel will not handle the call, then *fallbackAction* is going to be executed.
     *
     * @param cloudEvent AmigoCloudEvent is a parsed FCM event which has necessary Data
     * @param fallbackAction Is an action which can handle the Call when on background
     */
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
    /**
     * @return true indicates that this Event was properly handled by an Observer
     */
    fun onSuccess(call: Call): Boolean

    /**
     * @return true indicates that this Event was properly handled by an Observer
     */
    fun onJitsiCallEvent(callEvent: CallEvent): Boolean
}

class IncomingEventCallbackServiceImpl(
    ioDispatcher: CoroutineDispatcher,
    private val callRepository: CallRepository
) : IncomingEventCallbackService {

    private val scope = CoroutineScope(ioDispatcher)
    private var incomingEventCallback: IncomingEventCallback? = null

    override fun informWillJoin() {
        Timber.i("informWillJoin")
    }

    override fun informTerminated() {
        Timber.i("informTerminated")
        incomingEventCallback?.onJitsiCallEvent(CallEvent.FINISHED)
    }

    override fun informParticipantLeft() {
        Timber.i("informParticipantLeft")
        incomingEventCallback?.onJitsiCallEvent(CallEvent.FINISHED)
    }

    // Currently not important.
    override fun informJoined() {
        Timber.i("informJoined")
        incomingEventCallback?.onJitsiCallEvent(CallEvent.PARTICIPANT_JOINED)
    }

    // Currently not important.
    override fun informParticipantJoined() {
        Timber.i("informParticipantJoined")
        incomingEventCallback?.onJitsiCallEvent(CallEvent.PARTICIPANT_JOINED)
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

        // Download Call: needed for everything.
        val callResource = runBlocking(scope.coroutineContext) {
            callRepository.getCall(cloudEvent.entityId, true).first { it !is Resource.Loading }
        }
        when (callResource) {
            is Resource.Success -> {
                val call = callResource.value
                Timber.i("Handle cloudEvent: Resource.Success: $call")
                val hasBeenCompletedByObservers = incomingEventCallback?.onSuccess(call) ?: false
                /**
                 * When no Observer was connected, e.g. when App is in Background or was killed,
                 * we need to perform the fallbackAction (start Activity!)
                 */
                if (!hasBeenCompletedByObservers) {
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