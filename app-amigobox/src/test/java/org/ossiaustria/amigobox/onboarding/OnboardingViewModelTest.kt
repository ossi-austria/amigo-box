package org.ossiaustria.amigobox.onboarding

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.rules.TestRule
import org.ossiaustria.amigobox.EntityMocks
import org.ossiaustria.lib.commons.testing.TestCoroutineRule
import org.ossiaustria.lib.domain.auth.Account
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.services.AuthService

/**
 * Example test for Viewmodel
 *
 * 1. use a InstantTaskExecutorRule when using LiveData!
 * 2. use a fakre main (see setupBeforeEach())
 * 3. use runBlockingTest for coroutines which wait a long time
 */
@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
internal class OnboardingViewModelTest {

    // let us call the test subject "subject"
    // lateinit - must be defined in @Before method
    private lateinit var subject: OnboardingViewModel
    private lateinit var account: Account

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    // Use @Mockk to create a dummy mocking class - see example and mockk.io
    @MockK
    lateinit var authService: AuthService


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
        subject = OnboardingViewModel(coroutineRule.dispatcher, authService)
        account = EntityMocks.account()
        // "every" method call with "any" param "returns" a certain object
    }

    /**
     * Because we run coroutines (see viewModel!) we need to use "runBlockingTest"
     */
    @Test
    fun `register should trigger state SUCCESS for successful registering `() = runBlockingTest {
        // prepare test
        every {
            authService.register(any(), any(), any())
        } returns flow {
            emit(Resource.success(account))
        }

        subject.register("email", "password", "fullname")
        val value = subject.state.value
        advanceTimeBy(10)
        assertEquals(OnboardingState.RegisterSuccess(account), value)
    }

    /**
     * Because we run coroutines (see viewModel!) we need to use "runBlockingTest"
     */
    @Test
    fun `register should trigger state FAILURE for failure registering `() = runBlockingTest {
        // prepare test
        every {
            authService.register(any(), any(), any())
        } returns flow {
            emit(Resource.failure<Account>("some error"))
        }

        // run test subject
        subject.register("email", "password", "fullname")
        advanceTimeBy(100)
        assertTrue(subject.state.value is OnboardingState.RegisterFailed)
    }
}