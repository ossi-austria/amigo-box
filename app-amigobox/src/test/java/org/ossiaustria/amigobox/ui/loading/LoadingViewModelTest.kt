package org.ossiaustria.amigobox.ui.loading

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.*
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
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class LoadingViewModelTest {

    lateinit var subject: LoadingViewModel

    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val dispatcherProvider = TestDispatcherProvider(testCoroutineDispatcher)

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setupBeforeEach() {
        Dispatchers.setMain(mainThreadSurrogate)
        subject = LoadingViewModel(dispatcherProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `doFancyHeavyStuffOnBackground should login my user `() = runBlockingTest {
        subject.doFancyHeavyStuffOnBackground()

        advanceTimeBy(5_000)
        val value = subject.liveUserLogin.value

        assertEquals("user logged in!", value)
    }
}