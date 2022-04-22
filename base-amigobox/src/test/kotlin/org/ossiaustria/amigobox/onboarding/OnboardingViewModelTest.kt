package org.ossiaustria.amigobox.onboarding

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.rules.TestRule
import org.ossiaustria.amigobox.EntityMocks
import org.ossiaustria.amigobox.ui.loading.OnboardingState
import org.ossiaustria.amigobox.ui.loading.OnboardingViewModel
import org.ossiaustria.amigobox.ui.loading.SynchronisationService
import org.ossiaustria.lib.commons.testing.TestCoroutineRule
import org.ossiaustria.lib.domain.auth.Account
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.modules.UserContext
import org.ossiaustria.lib.domain.services.AuthService

@FlowPreview
internal class OnboardingViewModelTest {

    private lateinit var subject: OnboardingViewModel
    private lateinit var account: Account

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

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
    fun `login should trigger state SUCCESS for successful login `() = runTest {
        // prepare test
        coEvery {
            authService.login(any(), any())
        } returns Resource.success(account)


        subject.login("email", "password")
        testScheduler.advanceUntilIdle()

        subject.state.test().assertValue(OnboardingState.LoginSuccess(account))
    }

    /**
     * Because we run coroutines (see viewModel!) we need to use "runBlockingTest"
     */
    @Test
    fun `login should trigger state FAILURE for failure login `() = runTest {
        // prepare test
        coEvery {
            authService.login(any(), any())
        } returns
            Resource.failure<Account>(Exception("Error"))

        // run test subject
        subject.login("email", "password")
        testScheduler.advanceUntilIdle()

        assertTrue(subject.state.value is OnboardingState.LoginFailed)
    }
}