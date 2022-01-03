package org.ossiaustria.amigobox.ui.calls

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.ossiaustria.amigobox.BoxViewModel
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.CallState
import org.ossiaustria.lib.domain.models.enums.CallType
import org.ossiaustria.lib.domain.modules.UserContext
import org.ossiaustria.lib.domain.repositories.GroupRepository
import org.ossiaustria.lib.domain.repositories.finished
import org.ossiaustria.lib.domain.services.CallService
import org.ossiaustria.lib.jitsi.ui.JitsiWebrtcJsWebView
import timber.log.Timber
import java.util.*

class CallViewModel(
    ioDispatcher: CoroutineDispatcher,
    private val userContext: UserContext,
    private val callService: CallService,
    private val groupRepository: GroupRepository,
) : BoxViewModel(ioDispatcher) {

    val jitsiListener: JitsiWebrtcJsWebView.Listener = object : JitsiWebrtcJsWebView.Listener {
        override fun refreshParticipantsCount(count: Int) {}
        override fun refreshAudioMuted(isAudioMuted: Boolean) {
            _state.value?.let {
                if (it is CallViewState.Accepted) {
                    _state.postValue(it.copy(isMuted = isAudioMuted))
                }
            }
        }
    }
    private val _state: MutableLiveData<CallViewState> = MutableLiveData()
    private val _partner: MutableLiveData<Person> = MutableLiveData()
    private val _jitsiCommand: MutableLiveData<JitsiCallComposableCommand> =
        MutableLiveData(JitsiCallComposableCommand.Prepare)

    val state: LiveData<CallViewState> = _state
    val partner: LiveData<Person> = _partner
    val jitsiCommand: LiveData<JitsiCallComposableCommand> = _jitsiCommand

    private var activeCall: Call? = null

    /**
     * INCOMING calls are passed to this per FCM logic.
     * Prepare State and load Call & Person via Service
     */
    fun prepareIncomingCall(call: Call) = viewModelScope.launch {
        runRefreshingPerson(call.senderId)
        _state.postValue(CallViewState.Calling(call, false))
    }

    /**
     *  OUTGOING call ist created for a provided Person.
     *
     *  NOTE: The call has to saved to reuse the Jitsi JWT token later on!
     */
    fun createNewOutgoingCall(person: Person) = viewModelScope.launch {
        runRefreshingPerson(person.id)

        Timber.i("createCall: for person ${person}")

        val resource = callService.createCall(person, CallType.VIDEO)
        if (resource is Resource.Success) {
            activeCall = resource.value
            Timber.i("createCall: ->  ${activeCall}")
            _state.postValue(CallViewState.Calling(resource.value, true))
        } else if (resource is Resource.Failure) {
            Timber.e("acceptIncomingCall: ${resource.throwable}")
        }
    }

    private suspend fun runRefreshingPerson(personId: UUID) {
        Timber.i("runRefreshingPerson: search for  -> ${personId}")

        val resource =
            groupRepository.getGroup(userContext.person()!!.groupId, true).finished().first()
        if (resource.isSuccess) {
            Timber.i("runRefreshingPerson: refreshed group")
            _partner.postValue(resource.valueOrNull()?.members?.find { it.id == personId })
        } else if (resource is Resource.Failure) {
            Timber.e("refreshPerson: could not fetch group -> ${resource.throwable}")
        }

    }

    /**
     * Needed for both INCOMING & OUTGOING
     * Could happen at any time, but usually will happen when the other party performs an operation
     * like: ACCEPT, CANCEL, DENY, FINISH
     *
     * This will use CallViewModel and trigger a state change, see callViewModel.state
     */
    fun onObservedCallChanged(newCall: Call) {
        val currentState = _state.value

        if (currentState?.call != null && currentState.call.id != newCall.id) {
            Timber.w("Currently, a call is active, and a new Call cannot be accepted")
            viewModelScope.launch {
                callService.deny(newCall)
            }
            return
        }

        /**
         * Handle state changes via CallState. Transitions are limited and current CallViewState to be Calling or Started.
         *
         * NOTE: There is a CallViewState STARTED, but not CallState STARTED (because it is ACCEPTED)
         */
        if (currentState is CallViewState.Calling) {
            _state.value = when (newCall.callState) {
                CallState.ACCEPTED -> currentState.start(newCall)
                CallState.DENIED -> currentState.cancel(newCall)
                CallState.CANCELLED -> currentState.cancel(newCall)
                CallState.TIMEOUT -> currentState.timeout(newCall)
                else -> currentState
            }
            if (newCall.callState == CallState.ACCEPTED) {
                emitJitsiCommand(JitsiCallComposableCommand.EnterRoom)
            }

        } else if (currentState is CallViewState.Accepted && newCall.callState == CallState.FINISHED) {
            _state.value = currentState.finish(newCall)
            emitJitsiCommand(JitsiCallComposableCommand.Finish)

        } else {
            Timber.w("No support for $currentState and $newCall, do nothing")
        }
    }

    fun accept() = viewModelScope.launch {
        val callState = state.value
        if (callState is CallViewState.Calling && !callState.outgoing) {
            val resource = callService.accept(callState.call)
            if (resource is Resource.Success) {
                activeCall = resource.value
                _state.postValue(callState.start(resource.value))
                emitJitsiCommand(JitsiCallComposableCommand.EnterRoom)

            } else if (resource is Resource.Failure) {
                _state.postValue(callState.error(resource.throwable))
                Timber.e("accept: ${resource.throwable}")
            }
        }
    }

    fun deny() = viewModelScope.launch {
        val callState = state.value
        if (callState is CallViewState.Calling && !callState.outgoing) {
            val resource = callService.deny(callState.call)
            if (resource is Resource.Success) {
                _state.postValue(callState.cancel(resource.value))
            } else if (resource is Resource.Failure) {
                _state.postValue(callState.error(resource.throwable))
                Timber.e("accept: ${resource.throwable}")
            }
        }
    }

    fun cancel() = viewModelScope.launch {
        val callState = state.value
        if (callState is CallViewState.Calling && callState.outgoing) {
            val resource = callService.cancel(callState.call)
            if (resource is Resource.Success) {
                _state.postValue(callState.cancel(resource.value))
                emitJitsiCommand(JitsiCallComposableCommand.Finish)

            } else if (resource is Resource.Failure) {
                _state.postValue(callState.error(resource.throwable))
                Timber.e("accept: ${resource.throwable}")
            }
        }
    }

    fun finish() = viewModelScope.launch {
        val callState = state.value
        if (callState is CallViewState.Accepted) {
            val resource = callService.finish(callState.call)
            //Server needs fix for this 400 response
//                if (it is Resource.Success) {
            _state.postValue(callState.finish(callState.call))
//                }
            emitJitsiCommand(JitsiCallComposableCommand.Finish)

        }
    }

    fun onToggleAudio() = emitJitsiCommand(JitsiCallComposableCommand.ToggleAudio)

    private fun emitJitsiCommand(jitsiCallComposableCommand: JitsiCallComposableCommand) =
        viewModelScope.launch {
            _jitsiCommand.value = jitsiCallComposableCommand
            delay(500)
            _jitsiCommand.value = null
        }

    fun getToken(): String? = if (activeCall != null) {
        activeCall?.token!!
    } else null

}