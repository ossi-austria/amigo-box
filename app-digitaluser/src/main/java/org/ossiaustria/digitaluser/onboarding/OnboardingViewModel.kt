package org.ossiaustria.digitaluser.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.ossiaustria.lib.domain.auth.Account
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.services.AuthService
import timber.log.Timber
import javax.inject.Inject

/**
 * OnboardingState helps OnboardingViewModel to describe the current logical state of user flow and UI
 */
sealed class OnboardingState {
    object Init : OnboardingState()
    object Unauthenticated : OnboardingState()
    data class LoginFailed(val exception: Throwable) : OnboardingState()
    data class LoginSuccess(val account: Account) : OnboardingState()
    data class RegisterFailed(val exception: Throwable) : OnboardingState()
    data class RegisterSuccess(val account: Account) : OnboardingState()
}

/**
 * Every ViewModel needs  and an @Inject constructor like this.
 * Objects in the constructor (mostly Services) will be injected automatically
 *
 * OnboardingViewModel uses AuthService and OnboardingState to handle register, login etc
 * and prepare the data for UI and other local purposes
 */

class OnboardingViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val authService: AuthService
) : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _state: MutableLiveData<OnboardingState> = MutableLiveData(OnboardingState.Init)

    // The UI collects from this StateFlow to get its state updates
    // We use LiveData as they can be observed in the UI easily
    val state: LiveData<OnboardingState> = _state

    /**
     * Checks AuthService whether we already have a stored Account (we are authenticated) or not
     */
    fun onResume() {
        viewModelScope.launch(ioDispatcher) {
            authService.myAccount().collect {
                Timber.i(it.toString())
                if (it is Resource.Success) {
                    it.value.email
                } else {
                    Timber.d("$it")//_state.emit(OnboardingState.Unauthenticated)
                }
            }
        }
    }

    /**
     * When ViewModel gets destroyed, cancel the remaining open coroutine jobs
     */
    override fun onCleared() {
        super.onCleared()
        viewModelScope.coroutineContext.cancelChildren()
    }

    /**
     * Use the AuthService to perform a Login Request and store the result in the App.
     * This methods updates the _state which will inform the UI
     */
    fun login(email: String, password: String) {
        viewModelScope.launch(ioDispatcher) {
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
     * Use the AuthService to perform a Register Request and store the result in the App.
     * This methods updates the _state which will inform the UI
     */
    fun register(email: String, password: String, fullName: String) {
        viewModelScope.launch(ioDispatcher) {
            authService.register(email, password, fullName).collect {
                when (it) {
                    is Resource.Success -> _state.postValue(OnboardingState.RegisterSuccess(it.value))
                    is Resource.Failure -> _state.postValue(
                        OnboardingState.RegisterFailed(it.throwable ?: Exception(it.failureCause))
                    )
                    else -> Timber.d("$it")//_state.emit(OnboardingState.Unauthenticated)
                }
            }
        }
    }
}