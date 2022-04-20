package org.ossiaustria.amigobox.onboarding

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.ossiaustria.amigobox.EntityMocks
import org.ossiaustria.amigobox.ui.loading.SynchronisationService
import org.ossiaustria.amigobox.ui.loading.ViewModelTest
import org.ossiaustria.lib.domain.auth.Account
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.modules.UserContext
import org.ossiaustria.lib.domain.services.AuthService

/**
 * Example test for Viewmodel
 *
 * 1. use a InstantTaskExecutorRule when using LiveData!
 * 2. use a fakre main (see setupBeforeEach())
 * 3. use runBlockingTest for coroutines which wait a long time
 */
@FlowPreview
internal class OnboardingViewModelTest : ViewModelTest() {

    // let us call the test subject "subject"
    // lateinit - must be defined in @Before method
    private lateinit var subject: OnboardingViewModel
    private lateinit var account: Account

    // Use @Mockk to create a dummy mocking class - see example and mockk.io
    @MockK
    lateinit var authService: AuthService

    @MockK
    lateinit var userContext: UserContext

    @MockK
    lateinit var synchronisationService: SynchronisationService

    /**
     * The method with @Before will be called before EACH test
     *
     * usually several points:
     * 1. create test subject
     * 2. setup/initialise helpers
     * 3. create mocks
     */
    @Before
    fun setupBeforeEach() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        subject = OnboardingViewModel(
            userContext,
            synchronisationService,
            authService,
            coroutineRule.dispatcher,
        )
        account = EntityMocks.account()
        // "every" method call with "any" param "returns" a certain object
    }

    /**
     * Because we run coroutines (see viewModel!) we need to use "runBlockingTest"
     */
    @Test
    fun `login should trigger state SUCCESS for successful login `() = runBlockingTest {
        // prepare test
        every {
            authService.login(any(), any())
        } returns flow {
            emit(Resource.success(account))
        }

        subject.login("email", "password")
        val value = subject.state.value
        advanceTimeBy(10)
        assertEquals(OnboardingState.LoginSuccess(account), value)
    }

    /**
     * Because we run coroutines (see viewModel!) we need to use "runBlockingTest"
     */
    @Test
    fun `login should trigger state FAILURE for failure login `() = runBlockingTest {
        // prepare test
        every {
            authService.login(any(), any())
        } returns flow {
            emit(Resource.failure<Account>(Exception("Error")))
        }

        // run test subject
        subject.login("email", "password")
        advanceTimeBy(100)
        assertTrue(subject.state.value is OnboardingState.LoginFailed)
    }
}