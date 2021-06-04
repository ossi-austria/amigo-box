package org.ossiaustria.amigobox.timeline

import androidx.collection.arraySetOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.ossiaustria.amigobox.BoxViewModel
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Sendable
import org.ossiaustria.lib.domain.services.ServiceMocks
import org.ossiaustria.lib.domain.services.TimelineService
import javax.inject.Inject


/**
 * Every ViewModel needs @HiltViewModel and an @Inject constructor like this.
 * Objects in the constructor (mostly Services) will be injected automatically
 *
 * OnboardingViewModel uses AuthService and OnboardingState to handle register, login etc
 * and prepare the data for UI and other local purposes
 */
@HiltViewModel
class TimelineViewModel @Inject constructor(
        ioDispatcher: CoroutineDispatcher,
        private val timelineService: TimelineService
) : BoxViewModel(ioDispatcher) {

    private val _sendables: MutableLiveData<List<Sendable>> = MutableLiveData()
    val sendables: LiveData<List<Sendable>> = _sendables

    private var sendablesCache: Set<Sendable> = arraySetOf()

    fun onResume() {
        sendablesCache = arraySetOf()
    }

    fun loadAllSendables() {
        viewModelScope.launch(ioDispatcher) {
            timelineService.findWithPersons(ServiceMocks.MY_PERSON_ID, ServiceMocks.HER_PERSON_ID)
                .collect { resource ->
                    if (resource is Resource.Success) {
                        sendablesCache = sendablesCache.toMutableSet()
                            .apply { addAll(resource.value) }
                            .sortedBy { it.retrievedAt }
                            .toSet()
                        _sendables.postValue(sendablesCache.toList())
                    }
                }
        }
    }
}