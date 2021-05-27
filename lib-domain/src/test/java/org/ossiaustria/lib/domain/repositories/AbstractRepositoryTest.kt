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
import org.ossiaustria.lib.commons.TestDispatcherProvider
import org.ossiaustria.lib.domain.common.Effect
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
        flowList: Flow<Effect<List<DOMAIN>>>
    ) = coroutineScope {
        val firstResultLatch = CountDownLatch(1)
        val secondResultLatch = CountDownLatch(1)
        val thirdResultLatch = CountDownLatch(1)

        var results: MutableList<Effect<List<DOMAIN>>>? = null

        var resultCounter = 0
        val job = async(dispatcher) {
            flowList.collect { effect: Effect<List<DOMAIN>> ->
                if (results.isNullOrEmpty()) {
                    results = mutableListOf()
                }
                results?.add(effect)
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
        assert(outcome0.isSuccess)
        assert(outcome0.value!!.size == daoList.size)

        secondResultLatch.await()
        val outcome1 = results!![1]
        assert(outcome1.isLoading)

        thirdResultLatch.await()
        val outcome2 = results!![2]
        assert(outcome2.isSuccess)
        assert(outcome2.value!!.size == remoteList.size)
        job.cancelAndJoin()
    }
}