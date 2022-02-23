package org.ossiaustria.amigobox.ui.calls

import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.enums.CallState

/**
 * CallViewState describes the state machine needed for Calls
 */
sealed class CallViewState(
    open val call: Call,
    open val outgoing: Boolean
) {
    /**
     * A call was created and waits for ACCEPT, DENY or CANCEL
     */
    data class Calling(override val call: Call, override val outgoing: Boolean) :
        CallViewState(call, outgoing) {
        fun start(call: Call): CallViewState = Accepted(call, outgoing, false).also {
            if (call.callState != CallState.ACCEPTED)
                throw IllegalStateException("'Accepted' is not valid for ${call.callState}")
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

    /**
     * A created call was DENIED or CANCELLED by Caller or Callee
     */
    data class Cancelled(override val call: Call, override val outgoing: Boolean) :
        CallViewState(call, outgoing)

    /**
     * A call was created and then ACCEPTED, by you or other person
     */
    data class Accepted(
        override val call: Call,
        override val outgoing: Boolean,
        val isMuted: Boolean
    ) :
        CallViewState(call, outgoing) {
        fun finish(call: Call): CallViewState = Finished(call, outgoing)
        // TODO: add again after server fix
//        .also {
//            if (call.callState != CallState.FINISHED)
//                throw IllegalStateException("'Finished' is not valid for ${call.callState}")
//        }

        fun toggledMuted(): Accepted {
            return this.copy(isMuted = !this.isMuted)
        }
    }

    /**
     * Server, your App or other App decided it tool too long and interrupted the Call as timeout
     */
    data class Timeout(override val call: Call, override val outgoing: Boolean) :
        CallViewState(call, outgoing)

    /**
     * Caller or Callee finished Call properly
     */
    data class Finished(override val call: Call, override val outgoing: Boolean) :
        CallViewState(call, outgoing)

    data class Failure(
        override val call: Call,
        override val outgoing: Boolean,
        val error: Throwable?
    ) :
        CallViewState(call, outgoing)

    fun error(error: Throwable?): CallViewState = Failure(call, outgoing, error)
}