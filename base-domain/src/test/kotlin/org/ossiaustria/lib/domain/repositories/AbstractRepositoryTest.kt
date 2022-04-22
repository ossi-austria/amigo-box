package org.ossiaustria.lib.domain.repositories

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.assertTrue
import org.ossiaustria.lib.commons.TestDispatcherProvider
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.database.AppDatabaseImpl
import org.robolectric.annotation.Config
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@FlowPreview
@Config
internal open class AbstractRepositoryTest<ENTITY, DOMAIN> {

    lateinit var db: AppDatabaseImpl

    private val dispatcher = TestCoroutineDispatcher()
    val testDispatcherProvider = TestDispatcherProvider(dispatcher)

    @After
    fun tearDown() {
        db.close()
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    protected suspend fun testAllStates(
        daoList: List<ENTITY>,
        remoteList: List<DOMAIN>,
        flowList: Flow<Resource<List<DOMAIN>>>
    ) = withTimeout(2000L) {
        coroutineScope {
            val firstResultLatch = CountDownLatch(1)
            val secondResultLatch = CountDownLatch(1)
            val thirdResultLatch = CountDownLatch(1)

            var results: MutableList<Resource<List<DOMAIN>>>? = null

            var resultCounter = 0
            val job = async(dispatcher) {
                flowList.collect { resource: Resource<List<DOMAIN>> ->
                    if (results.isNullOrEmpty()) {
                        results = mutableListOf()
                    }
                    results?.add(resource)
                    resultCounter += 1
                    when (resultCounter) {
                        1 -> firstResultLatch.countDown()
                        2 -> secondResultLatch.countDown()
                        3 -> thirdResultLatch.countDown()
                    }
                }
            }

            firstResultLatch.await(2, TimeUnit.SECONDS)
            val outcome0 = results!![0]
            assertTrue(outcome0 is Resource.Success)
            assertTrue(outcome0.valueOrNull()!!.size == daoList.size)

            secondResultLatch.await(2, TimeUnit.SECONDS)
            val outcome1 = results!![1]
            assertTrue(outcome1.isLoading)

            thirdResultLatch.await(2, TimeUnit.SECONDS)
            val outcome2 = results!![2]
            assertTrue(outcome2.isSuccess)
            assertTrue(outcome2.valueOrNull()!!.size == remoteList.size)
            job.cancelAndJoin()
        }
    }
}
