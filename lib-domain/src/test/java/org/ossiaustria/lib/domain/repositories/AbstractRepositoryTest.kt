package org.ossiaustria.lib.domain.repositories

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Assert.assertTrue
import org.ossiaustria.lib.commons.TestDispatcherProvider
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.database.AppDatabaseImpl
import java.util.concurrent.CountDownLatch

@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
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
    ) = coroutineScope {
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

        firstResultLatch.await()
        val outcome0 = results!![0]
        assertTrue(outcome0 is Resource.Success)
        assertTrue(outcome0.valueOrNull()!!.size == daoList.size)

        secondResultLatch.await()
        val outcome1 = results!![1]
        assertTrue(outcome1.isLoading)

        thirdResultLatch.await()
        val outcome2 = results!![2]
        assertTrue(outcome2.isSuccess)
        assertTrue(outcome2.valueOrNull()!!.size == remoteList.size)
        job.cancelAndJoin()
    }
}
