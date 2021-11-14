package org.ossiaustria.amigobox.timeline

import android.os.CountDownTimer
import androidx.collection.arraySetOf
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
import org.ossiaustria.lib.domain.models.enums.CallState
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

    private val _sendables: MutableLiveData<List<Sendable>> = MutableLiveData(emptyList())
    val sendables: LiveData<List<Sendable>> = _sendables

    private var sendablesCache: Set<Sendable> = arraySetOf()

    private val _navigationState = MutableLiveData(GalleryNavState.PLAY)
    val navigationState: MutableLiveData<GalleryNavState> = _navigationState

    private val _autoState = MutableLiveData(AutoState.CHANGE)
    val autoState: MutableLiveData<AutoState> = _autoState

    private val _currentGalleryIndex = MutableLiveData(0)
    val currentGalleryIndex: MutableLiveData<Int> = _currentGalleryIndex

    // Timer variables
    private var countDownTimer: CountDownTimer? = null

    private val _time = MutableLiveData(CountdownFormat.TIME_COUNTDOWN.formatTime())
    val time: LiveData<String> = _time

    // not yet needed but can be used to track progress
    private val _progress = MutableLiveData(1.00F)
    val progress: LiveData<Float> = _progress

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    // from repository
    private val _sendablePerson: MutableLiveData<Person> = MutableLiveData()
    val sendablePerson: LiveData<Person> = _sendablePerson

    private val _personsMap: MutableLiveData<Map<UUID, String>> = MutableLiveData()
    val personsMap: LiveData<Map<UUID, String>> = _personsMap

    private val _personsList: MutableLiveData<List<Person>> = MutableLiveData()
    val personsList: LiveData<List<Person>> = _personsList

    val centerPerson = userContext.person()

    fun onResume() {
        sendablesCache = arraySetOf()
    }

    fun loadAllSendables() {
        //resest Sendables List and sendablesCache
        _sendables.value = emptyList()
        sendablesCache = emptySet()

        //fetch Sendables from Repository
        viewModelScope.launch(ioDispatcher) {
            timelineService.findWithPersons(ServiceMocks.MY_PERSON_ID, ServiceMocks.HER_PERSON_ID)
                .collect { resource ->
                    if (resource is Resource.Success) {
                        sendablesCache = sendablesCache.toMutableSet()
                            .apply { addAll(resource.value) }
                            .sortedBy { it.retrievedAt }
                            .toSet()
                        //filtering sendables and posting them here
                        _sendables.postValue(filterSendables(sendablesCache.toList()))
                    }
                }
        }
    }

    fun loadPersons() {
        viewModelScope.launch {
            groupRepository.getAllGroups(true).collect { resource ->
                if (resource.isSuccess && !resource.valueOrNull().isNullOrEmpty()) {
                    val groups = resource.valueOrNull()
                    groups?.firstOrNull()?.members?.let {
                        createMap(it)
                        _personsList.value = it
                    }
                } else {
                    createMap(emptyList())
                    _personsList.value = emptyList()
                }
            }
        }
    }

    fun findName(personId: UUID): String? = personsMap.value?.get(personId)

    private fun createMap(personsList: List<Person>) {
        _personsMap.value = personsList.map { it.id to it.name }.toMap()
    }

    fun findPerson(personId: UUID): Person? {
        val person = _personsList.value?.firstOrNull {
            it.id == personId
        }
        return person
    }

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

        _isPlaying.value = true
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
        _isPlaying.value = isPlaying
        _time.value = text
        _progress.value = progress
    }

    private fun filterSendables(list: List<Sendable>): List<Sendable> {
        val sendables = list.filter {
            when (it) {
                is Call -> (it.callState == CallState.TIMEOUT ||
                    it.callState == CallState.FINISHED)
                else -> true
            }
        }
        return sendables
    }

    fun initTimer() {
        cancelTimer()
        startTimer()
        setNavigationState(GalleryNavState.PLAY)
    }

}