package org.ossiaustria.amigobox.calls

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.ossiaustria.amigobox.BoxViewModel
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.CallState
import org.ossiaustria.lib.domain.models.enums.CallType
import org.ossiaustria.lib.domain.modules.UserContext
import org.ossiaustria.lib.domain.repositories.GroupRepository
import org.ossiaustria.lib.domain.services.CallService
import timber.log.Timber
import java.util.*

class CallViewModel(
    ioDispatcher: CoroutineDispatcher,
    private val userContext: UserContext,
    private val callService: CallService,
    private val groupRepository: GroupRepository,
) : BoxViewModel(ioDispatcher) {

    private val _state: MutableLiveData<CallViewState> = MutableLiveData()
    private val _partner: MutableLiveData<Person> = MutableLiveData()

    val state: LiveData<CallViewState> = _state
    val partner: LiveData<Person> = _partner

    private var activeCall: Call? = null

    fun prepareIncomingCall(call: Call) = viewModelScope.launch {
        _state.postValue(CallViewState.Calling(call, false))
        refreshPerson(call.senderId)
    }

    fun createNewOutgoingCall(person: Person) = viewModelScope.launch {
        callService.createCall(person, CallType.VIDEO).collect { resource ->
            if (resource is Resource.Success) {
                activeCall = resource.value
                _state.postValue(CallViewState.Calling(resource.value, true))
            } else if (resource is Resource.Failure) {
                Timber.e("acceptIncomingCall: ${resource.throwable}")
            }
        }
        refreshPerson(person.id)
    }

    private suspend fun refreshPerson(personId: UUID) {
        groupRepository.getGroup(userContext.person()!!.groupId, true).collect { resource ->
            if (resource.isSuccess) {
                _partner.postValue(resource.valueOrNull()?.members?.find { it.id == personId })
            } else if (resource is Resource.Failure) {
                Timber.e("refreshPerson: could not fetch group -> ${resource.throwable}")
            }
        }
    }

    fun onObservedCallChanged(newCall: Call) {
        val currentState = _state.value

        if (currentState is CallViewState.Calling) {
            _state.value = when (newCall.callState) {
                CallState.ACCEPTED -> currentState.start(newCall)
                CallState.STARTED -> currentState.start(newCall)
                CallState.DENIED -> currentState.cancel(newCall)
                CallState.CANCELLED -> currentState.cancel(newCall)
                CallState.TIMEOUT -> currentState.timeout(newCall)
                else -> currentState
            }
        } else if (currentState is CallViewState.Started && newCall.callState == CallState.FINISHED) {
            _state.value = currentState.finish(newCall)
        } else {
            Timber.w("No support for $currentState and $newCall, do nothing")
        }
    }

    fun accept() = viewModelScope.launch {
        val callState = state.value
        if (callState is CallViewState.Calling && !callState.outgoing) {
            callService.accept(callState.call).collect {
                if (it is Resource.Success) {
                    activeCall = it.value
                    _state.postValue(callState.start(it.value))
                } else if (it is Resource.Failure) {
                    _state.postValue(callState.error(it.throwable))
                    Timber.e("accept: ${it.throwable}")
                }
            }
        }
    }

    fun deny() = viewModelScope.launch {
        val callState = state.value
        if (callState is CallViewState.Calling && !callState.outgoing) {
            callService.deny(callState.call).collect {
                if (it is Resource.Success) {
                    _state.postValue(callState.cancel(it.value))
                } else if (it is Resource.Failure) {
                    _state.postValue(callState.error(it.throwable))
                    Timber.e("accept: ${it.throwable}")
                }
            }
        }
    }

    fun cancel() = viewModelScope.launch {
        val callState = state.value
        if (callState is CallViewState.Calling && callState.outgoing) {
            callService.cancel(callState.call).collect {
                if (it is Resource.Success) {
                    _state.postValue(callState.cancel(it.value))
                } else if (it is Resource.Failure) {
                    _state.postValue(callState.error(it.throwable))
                    Timber.e("accept: ${it.throwable}")
                }
            }
        }
    }

    fun finish() = viewModelScope.launch {
        val callState = state.value
        if (callState is CallViewState.Started) {
            callService.finish(callState.call).collect {
                if (it is Resource.Success) {
                    _state.postValue(callState.finish(it.value))
                }
            }
        }
    }

    fun getToken(): String? = if (activeCall != null) {
        activeCall?.token!!
    } else null

}