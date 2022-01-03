package org.ossiaustria.amigobox.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ossiaustria.amigobox.ui.commons.NavigationViewModel
import org.ossiaustria.amigobox.ui.loading.SynchronisationService
import org.ossiaustria.lib.domain.auth.Account
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.modules.UserContext
import org.ossiaustria.lib.domain.services.AuthService
import timber.log.Timber

/**
 * OnboardingState helps OnboardingViewModel to describe the current logical state of user flow and UI
 */
sealed class OnboardingState {
    object NotLoggedIn : OnboardingState()
    data class IsLoggedIn(val account: Account, val person: Person) : OnboardingState()
    data class LoginFailed(val exception: Throwable) : OnboardingState()
    data class LoginSuccess(val account: Account) : OnboardingState()
}

/**
 * Every ViewModel needs  and an @Inject constructor like this.
 * Objects in the constructor (mostly Services) will be injected automatically
 *
 * OnboardingViewModel uses AuthService and OnboardingState to handle register, login etc
 * and prepare the data for UI and other local purposes
 */
class OnboardingViewModel(
    private val userContext: UserContext,
    private val synchronisationService: SynchronisationService,
    private val authService: AuthService,
    ioDispatcher: CoroutineDispatcher
) : NavigationViewModel() {

    // Backing property to avoid state updates from other classes
    private val _state: MutableLiveData<OnboardingState> =
        MutableLiveData(OnboardingState.NotLoggedIn)

    // The UI collects from this StateFlow to get its state updates
    // We use LiveData as they can be observed in the UI easily
    val state: LiveData<OnboardingState> = _state

    private val coContext = ioDispatcher + Job()

    fun loadAccount() {
        viewModelScope.launch(coContext) {
            val person = userContext.person()
            val account = userContext.account()

            if (userContext.available()) {
                synchronisationService.syncEverything()
                _state.postValue(
                    OnboardingState.IsLoggedIn(account!!, person!!)
                )
            } else {
                _state.postValue(OnboardingState.NotLoggedIn)
            }
        }
    }

    /**
     * Use the AuthService to perform a Login Request and store the result in the App.
     * This methods updates the _state which will inform the UI
     */
    fun login(email: String, password: String) {
        viewModelScope.launch(coContext) {
            authService.login(email, password).collect {
                when (it) {
                    is Resource.Success -> _state.postValue(OnboardingState.LoginSuccess(it.value))
                    is Resource.Failure -> _state.postValue(OnboardingState.LoginFailed(it.throwable!!))
                    else -> Timber.d("$it")//_state.emit(OnboardingState.Unauthenticated)
                }
            }
        }
    }

    /**
     * Use the AuthService to perform a Login Request and store the result in the App.
     * This methods updates the _state which will inform the UI
     */
    fun loginPerToken(token: String) {
        viewModelScope.launch(coContext) {
            authService.login(token, token).collect {
                when (it) {
                    is Resource.Success -> _state.postValue(OnboardingState.LoginSuccess(it.value))
                    is Resource.Failure -> _state.postValue(OnboardingState.LoginFailed(it.throwable!!))
                    else -> Timber.d("$it")//_state.emit(OnboardingState.Unauthenticated)
                }
            }
        }
    }

    fun logout(callback: () -> Unit) {
        viewModelScope.launch(coContext) {
            authService.logout().collect {
                when (it) {
                    is Resource.Success -> _state.postValue(OnboardingState.NotLoggedIn)
                    else -> Timber.d("$it")
                }
                withContext(Dispatchers.Main) {
                    callback()
                }
            }
        }
    }

    /**
     * When ViewModel gets destroyed, cancel the remaining open coroutine jobs
     */
    public override fun onCleared() {
        super.onCleared()
        viewModelScope.coroutineContext.cancelChildren()
    }
}