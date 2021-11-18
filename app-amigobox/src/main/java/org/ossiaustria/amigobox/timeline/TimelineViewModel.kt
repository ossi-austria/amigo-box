package org.ossiaustria.amigobox.timeline

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.ossiaustria.amigobox.BoxViewModel
import org.ossiaustria.amigobox.ui.autoplay.AutoState
import org.ossiaustria.amigobox.ui.autoplay.CountdownFormat
import org.ossiaustria.amigobox.ui.autoplay.GalleryNavState
import org.ossiaustria.amigobox.ui.autoplay.formatTime
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
) : BoxViewModel(ioDispatcher) {

    val centerPerson = userContext.person()

    private val _sendables: MutableLiveData<List<Sendable>> = MutableLiveData(emptyList())
    val sendables: LiveData<List<Sendable>> = _sendables

    private val _navigationState = MutableLiveData(GalleryNavState.PLAY)
    val navigationState: MutableLiveData<GalleryNavState> = _navigationState

    private val _autoState = MutableLiveData(AutoState.CHANGE)
    val autoState: MutableLiveData<AutoState> = _autoState

    private val _currentGalleryIndex = MutableLiveData(0)
    val currentGalleryIndex: MutableLiveData<Int> = _currentGalleryIndex

    private val _time = MutableLiveData(CountdownFormat.TIME_COUNTDOWN.formatTime())
    val time: LiveData<String> = _time

    private var _personsIdMap: Map<UUID, Person> = emptyMap()

    // Timer variables
    private var countDownTimer: CountDownTimer? = null

    fun loadAllSendables() {
        //reset Sendables List and sendablesCache
        _sendables.value = emptyList()

        //fetch Sendables from Repository
        viewModelScope.launch(ioDispatcher) {
            timelineService.findWithPersons(ServiceMocks.MY_PERSON_ID, ServiceMocks.HER_PERSON_ID)
                .collect { resource ->
                    if (resource is Resource.Success) {
                        _sendables.postValue(
                            filterPresentableSendables(resource.value)
                                .sortedBy { it.retrievedAt })
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

    fun findName(personId: UUID): String? = _personsIdMap[personId]?.name

    private fun createMaps(personsList: List<Person>) {
        _personsIdMap = personsList.map { it.id to it }.toMap()
    }

    fun findPerson(personId: UUID): Person? = _personsIdMap[personId]

    // set states with following methods
    fun setNavigationState(navState: GalleryNavState) {
        _navigationState.value = navState
    }

    fun setGalleryIndex(currentGalleryIndex: Int) {
        _currentGalleryIndex.value = currentGalleryIndex
    }

    fun setAutoState(autoState: AutoState) {
        _autoState.value = autoState
    }

    fun pauseTimer() {
        countDownTimer?.cancel()
        handleTimerValues(false, CountdownFormat.TIME_COUNTDOWN.formatTime(), 1.0F)
    }

    fun startTimer() {
        countDownTimer = object : CountDownTimer(CountdownFormat.TIME_COUNTDOWN, 1000) {

            override fun onTick(millisRemaining: Long) {
                val progressValue = millisRemaining.toFloat() / CountdownFormat.TIME_COUNTDOWN
                handleTimerValues(true, millisRemaining.formatTime(), progressValue)
            }

            override fun onFinish() {
                startTimer()
            }
        }.start()
    }

    fun cancelTimer() {
        countDownTimer?.cancel()
    }

    private fun handleTimerValues(
        isPlaying: Boolean,
        text: String,
        progress: Float
    ) {
        _time.value = text
    }

    private fun filterPresentableSendables(sendables: List<Sendable>) = sendables.filter {
        if (it is Call) it.isDone()
        else true
    }

    fun initTimer() {
        cancelTimer()
        startTimer()
        setNavigationState(GalleryNavState.PLAY)
    }

}