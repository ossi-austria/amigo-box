package org.ossiaustria.amigobox.ui.loading

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.ossiaustria.lib.commons.TestDispatcherProvider

@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
//@RunWith(AndroidJUnit4ClassRunner::class)
class LoadingViewModelTest {

    lateinit var subject: LoadingViewModel

    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val dispatcherProvider = TestDispatcherProvider(testCoroutineDispatcher)

    @Before
    fun setupBeforeEach() {
        subject = LoadingViewModel(dispatcherProvider)
    }

    //    @ExperimentalCoroutinesApi
    @Test
    fun `doFancyHeavyStuffOnBackground should login my user `() {
        subject.doFancyHeavyStuffOnBackground()

//        advanceTimeBy(5_000)
        Thread.sleep(5000)
        val value = subject.liveUserLogin.value

        assertEquals("user logged in!", value)
    }
}