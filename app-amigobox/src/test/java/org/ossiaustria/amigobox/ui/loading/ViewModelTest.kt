package org.ossiaustria.amigobox.ui.loading

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.ossiaustria.lib.commons.TestDispatcherProvider
import org.ossiaustria.lib.commons.testing.TestCoroutineRule

@ExperimentalCoroutinesApi
open class ViewModelTest {

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    protected val testCoroutineDispatcher = TestCoroutineDispatcher()
    protected val dispatcherProvider = TestDispatcherProvider(testCoroutineDispatcher)

    @Before
    fun setupDispatchersBeforeEach() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @After
    fun dispatcherTearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        testCoroutineDispatcher.cleanupTestCoroutines()
    }
}
