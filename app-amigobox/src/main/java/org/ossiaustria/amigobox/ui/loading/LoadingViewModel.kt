package org.ossiaustria.amigobox.ui.loading

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.ossiaustria.amigobox.ui.commons.NavigationViewModel
import org.ossiaustria.lib.commons.DispatcherProvider
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.modules.UserContext
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val userContext: UserContext,
    private val synchronisationService: SynchronisationService,
) :
    NavigationViewModel() {

    private val _state: MutableLiveData<Resource<Boolean>> = MutableLiveData(Resource.loading())
    val liveState: LiveData<Resource<Boolean>> = _state

    private val _person: MutableLiveData<Person> = MutableLiveData()
    val livePerson: LiveData<Person> = _person

    private val ioScope = CoroutineScope(dispatcherProvider.io() + Job())

    fun loadAccount() {
        ioScope.launch {
            val person = userContext.person()

            if (userContext.available()) {
                synchronisationService.syncGroup()
                _person.postValue(person)
                _state.postValue(Resource.success(userContext.available()))
            } else {
                _state.postValue(Resource.failure("Not loggedin"))
                startLogin()
            }
        }
    }

    fun startPersonDetail() {
        val person = userContext.person()
        navigator?.toPersonDetail(person!!)
    }

    fun startTimeline() {
        navigator?.toTimeline()
    }

    fun startHome() {
        navigator?.toHome()
    }

    fun startJitsi() {
        navigator?.toJitsiCall()
    }

    fun startLogin() {
        navigator?.toLogin()
    }

    fun startContacts() {
        navigator?.toContacts()
    }

    fun startAlbums() {
        navigator?.toAlbums()
    }

    fun startGallery() {
        navigator?.toImageGallery()
    }
}