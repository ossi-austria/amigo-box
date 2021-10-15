package org.ossiaustria.digitaluser.ui.loading

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.rules.TestRule
import org.ossiaustria.lib.commons.TestDispatcherProvider

/**
 * Example test for Viewmodel
 *
 * 1. use a InstantTaskExecutorRule when using LiveData!
 * 2. use a fakre main (see setupBeforeEach())
 * 3. use runBlockingTest for coroutines which wait a long time
 */
@FlowPreview
@ExperimentalCoroutinesApi
internal class LoadingViewModelTest {

    lateinit var subject: LoadingViewModel

    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val dispatcherProvider = TestDispatcherProvider(testCoroutineDispatcher)

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setupBeforeEach() {
        Dispatchers.setMain(testCoroutineDispatcher)
        subject = LoadingViewModel(dispatcherProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        testCoroutineDispatcher.cleanupTestCoroutines()
    }

    /**
     * Because we run coroutines (see viewModel!) we need to use "runBlockingTest"
     */
    @Test
    fun `doFancyHeavyStuffOnBackground should login my user `() = runBlockingTest {
        subject.doFancyHeavyStuffOnBackground()

        advanceTimeBy(5_000)
        val value = subject.liveUserLogin.value

        assertEquals("user logged in!", value)
    }
}