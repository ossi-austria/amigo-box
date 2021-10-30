package org.ossiaustria.amigobox.calls

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.ossiaustria.amigobox.BoxViewModel
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.services.IncomingEventCallback
import org.ossiaustria.lib.domain.services.IncomingEventCallbackService
import timber.log.Timber

class IncomingEventsViewModel(
    ioDispatcher: CoroutineDispatcher,
    private val incomingEventCallbackService: IncomingEventCallbackService,
) : BoxViewModel(ioDispatcher) {

    private val _notifiedCall: MutableLiveData<Call> = MutableLiveData()
    val notifiedCall: LiveData<Call> = _notifiedCall

    fun startListening() = viewModelScope.launch {
        incomingEventCallbackService.observe(object : IncomingEventCallback {
            override fun onSuccess(call: Call) {
                _notifiedCall.postValue(call)
            }

            override fun onError(e: Throwable?) {
                Timber.e(e)
            }
        })
    }

    override fun onCleared() {
        incomingEventCallbackService.stopObserving()
        super.onCleared()
    }

}