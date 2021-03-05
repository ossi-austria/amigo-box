package org.ossiaustria.amigobox.ui.loading

import TestDispatcherProviderImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

@ExperimentalCoroutinesApi
class LoadingViewModelTest {

    lateinit var subject: LoadingViewModel

    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val dispatcherProvider = TestDispatcherProviderImpl(testCoroutineDispatcher)

    @Before
    fun setupBeforeEach() {
        subject = LoadingViewModel(dispatcherProvider)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `doFancyHeavyStuffOnBackground should login my user `() = runBlockingTest {
        subject.doFancyHeavyStuffOnBackground()

        advanceTimeBy(5_000)
        Thread.sleep(5000)
        val value = subject.liveUserLogin.value

        assertEquals("user logged in!", value)
    }
}