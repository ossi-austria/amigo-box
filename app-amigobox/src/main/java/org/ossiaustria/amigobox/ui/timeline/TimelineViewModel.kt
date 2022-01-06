package org.ossiaustria.amigobox.ui.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.ossiaustria.amigobox.TimedSlideshowViewModel
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.Sendable
import org.ossiaustria.lib.domain.modules.UserContext
import org.ossiaustria.lib.domain.repositories.GroupRepository
import org.ossiaustria.lib.domain.services.ServiceMocks
import org.ossiaustria.lib.domain.services.TimelineService
import java.util.*

/**
 * Every ViewModel needs  and an @Inject constructor like this.
 * Objects in the constructor (mostly Services) will be injected automatically
 *
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
        //reset Sendables List and sendablesCache
        _sendables.value = emptyList()

        //fetch Sendables from Repository
        viewModelScope.launch(ioDispatcher) {
            timelineService.findWithPersons(ServiceMocks.MY_PERSON_ID, ServiceMocks.HER_PERSON_ID)
                .collect { resource ->
                    if (resource is Resource.Success) {
                        val sortedBy =
                            filterPresentableSendables(resource.value).sortedBy { it.retrievedAt }
                        slideShowManager.size = sortedBy.size
                        _sendables.postValue(sortedBy)
                    }
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

    private fun filterPresentableSendables(sendables: List<Sendable>) = sendables.filter {
        if (it is Call) it.isDone()
        else true
    }
}