package org.ossiaustria.amigobox.calls

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.ossiaustria.amigobox.BoxViewModel
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.services.CallEvent
import org.ossiaustria.lib.domain.services.IncomingEventCallback
import org.ossiaustria.lib.domain.services.IncomingEventCallbackService
import timber.log.Timber

class IncomingEventsViewModel(
    ioDispatcher: CoroutineDispatcher,
    private val incomingEventCallbackService: IncomingEventCallbackService,
) : BoxViewModel(ioDispatcher) {

    private val _notifiedCall: MutableLiveData<Call> = MutableLiveData()
    val notifiedCall: LiveData<Call> = _notifiedCall

    private val _notifiedCallEvent: MutableLiveData<CallEvent> = MutableLiveData()
    val notifiedCallEvent: LiveData<CallEvent> = _notifiedCallEvent

    init {
        viewModelScope.launch {
            incomingEventCallbackService.observe(object : IncomingEventCallback {
                override fun onSuccess(call: Call): Boolean {
                    _notifiedCall.postValue(call)
                    return _notifiedCall.hasActiveObservers() || notifiedCall.hasActiveObservers()
                }

                override fun onJitsiCallEvent(callEvent: CallEvent): Boolean {
                    Timber.i("onJitsiCallEvent: $callEvent")
                    _notifiedCallEvent.postValue(callEvent)
                    return _notifiedCallEvent.hasActiveObservers() || notifiedCallEvent.hasActiveObservers()

                }
            })
        }
    }

    override fun onCleared() {
        incomingEventCallbackService.stopObserving()
        super.onCleared()
    }

    fun clearCall() {
        _notifiedCall.value = null
        _notifiedCallEvent.value = null
    }

}