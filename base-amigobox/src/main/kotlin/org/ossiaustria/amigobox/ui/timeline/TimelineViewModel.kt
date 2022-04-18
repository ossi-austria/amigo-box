package org.ossiaustria.amigobox.ui.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ossiaustria.amigobox.TimedSlideshowViewModel
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.Sendable
import org.ossiaustria.lib.domain.modules.UserContext
import org.ossiaustria.lib.domain.repositories.GroupRepository
import org.ossiaustria.lib.domain.services.TimelineService
import java.util.*

/**
 * OnboardingViewModel uses AuthService and OnboardingState to handle register, login etc
 * and prepare the data for UI and other local purposes
 */

class TimelineViewModel(
    ioDispatcher: CoroutineDispatcher,
    private val timelineService: TimelineService,
    private val groupRepository: GroupRepository,
    userContext: UserContext
) : TimedSlideshowViewModel(ioDispatcher) {

    val centerPerson = userContext.person()

    private val _sendables: MutableLiveData<List<Sendable>> = MutableLiveData(emptyList())
    val sendables: LiveData<List<Sendable>> = _sendables

    private var _personsIdMap: Map<UUID, Person> = emptyMap()

    fun loadAllSendables() {
        //fetch Sendables from Repository
        viewModelScope.launch(ioDispatcher) {
            val sendables = timelineService.findWithReceiver(centerPerson!!.id)
            val sortedBy = filterPresentableSendables(sendables)
                .sortedBy { it.sentAt }
                .reversed()
            slideShowManager.size = sortedBy.size
            _sendables.postValue(sortedBy)
            withContext(Dispatchers.Main) {
                startTimer()
            }
        }
    }

    fun loadPersons() {
        viewModelScope.launch {
            groupRepository.getAllGroups(true).collect { resource ->
                if (resource is Resource.Success && !resource.value.isNullOrEmpty()) {
                    val groups = resource.value
                    groups.firstOrNull()?.members?.let {
                        createMaps(it)
                    }
                }
            }
        }
    }

    fun findPerson(personId: UUID): Person? = _personsIdMap[personId]

    private fun createMaps(personsList: List<Person>) {
        _personsIdMap = personsList.map { it.id to it }.toMap()
    }

    /**
     * Filter sendables, mostly calls: just show Calls which are finished.
     */
    private fun filterPresentableSendables(sendables: List<Sendable>) = sendables.filter {
        if (it is Call) it.isDone()
        else true
    }
}