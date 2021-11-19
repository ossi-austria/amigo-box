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

/**
 * Binds to the long-living singleton IncomingEventCallbackService and handles life-cycle-aware Observers.
 *
 * Important: Events can just be received and used, if Observers are active!
 *
 * @see IncomingEventCallback for more details
 */
class IncomingEventsViewModel(
    ioDispatcher: CoroutineDispatcher,
    private val incomingEventCallbackService: IncomingEventCallbackService,
) : BoxViewModel(ioDispatcher) {

    /**
     * Propagated FCM Events for INCOMING and OUTGOING Calls
     */
    private val _notifiedCall: MutableLiveData<Call> = MutableLiveData()
    val notifiedCall: LiveData<Call> = _notifiedCall

    /**
     * Propagated local JitsiBroadcast Events for INCOMING and OUTGOING Calls
     */
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

    /**
     * Important: If all Observers leave (pause, destroy, etc) this ViewModel will not handle events.
     */
    override fun onCleared() {
        Timber.i("stopObserving all FCM and Jitsi events")
        incomingEventCallbackService.stopObserving()
        super.onCleared()
    }

    /**
     * LifeData has to cleared to avoid duplicated events..
     */
    fun clearCall() {
        _notifiedCall.value = null
        _notifiedCallEvent.value = null
    }

}