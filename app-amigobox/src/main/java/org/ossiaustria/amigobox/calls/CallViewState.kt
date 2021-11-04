package org.ossiaustria.amigobox.calls

import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.enums.CallState

/**
 * CallViewState describes the state machine needed for Calls
 */
sealed class CallViewState(
    open val call: Call,
    open val outgoing: Boolean,
    val isActive: Boolean?,
) {
    data class Calling(override val call: Call, override val outgoing: Boolean) :
        CallViewState(call, outgoing, false) {
        fun start(call: Call): CallViewState = Started(call, outgoing).also {
            if (call.callState != CallState.STARTED && call.callState != CallState.ACCEPTED)
                throw IllegalStateException("'Started' is not valid for ${call.callState}")
        }

        fun cancel(call: Call): CallViewState = Cancelled(call, outgoing).also {
            if (call.callState != CallState.CANCELLED && call.callState != CallState.DENIED)
                throw IllegalStateException("'Cancelled' is not valid for ${call.callState}")
        }

        fun timeout(call: Call): CallViewState = Timeout(call, outgoing).also {
            if (call.callState != CallState.TIMEOUT)
                throw IllegalStateException("'Timeout' is not valid for ${call.callState}")
        }
    }

    data class Cancelled(override val call: Call, override val outgoing: Boolean) :
        CallViewState(call, outgoing, false)

    data class Started(override val call: Call, override val outgoing: Boolean) :
        CallViewState(call, outgoing, true) {
        fun finish(call: Call): CallViewState = Finished(call, outgoing).also {
            if (call.callState != CallState.FINISHED)
                throw IllegalStateException("'Finished' is not valid for ${call.callState}")
        }
    }

    data class Timeout(override val call: Call, override val outgoing: Boolean) :
        CallViewState(call, outgoing, false)

    data class Finished(override val call: Call, override val outgoing: Boolean) :
        CallViewState(call, outgoing, false)

    data class Failure(
        override val call: Call,
        override val outgoing: Boolean,
        val error: Throwable?
    ) :
        CallViewState(call, outgoing, false)

    fun error(error: Throwable?): CallViewState = Failure(call, outgoing, error)
}