package org.ossiaustria.lib.domain.repositories

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ossiaustria.lib.commons.TestDispatcherProvider
import org.ossiaustria.lib.domain.api.AuthorApi
import org.ossiaustria.lib.domain.common.Outcome
import org.ossiaustria.lib.domain.database.AppDatabaseImpl
import org.ossiaustria.lib.domain.database.AuthorDao
import org.ossiaustria.lib.domain.models.Author
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.CountDownLatch

@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class AuthorRepositoryTest {


    lateinit var subject: AuthorRepository
    lateinit var dao: AuthorDao
    lateinit var db: AppDatabaseImpl


    val dispatcher = TestCoroutineDispatcher()
    private val testDispatcherProvider = TestDispatcherProvider(dispatcher)

    @RelaxedMockK
    lateinit var mockAuthorApi: AuthorApi

    @Before
    fun before() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, AppDatabaseImpl::class.java).build()
        dao = db.authorDao()
        MockKAnnotations.init(this, relaxUnitFun = true)

        subject = AuthorRepositoryImpl(mockAuthorApi, dao, testDispatcherProvider)
    }

    @InternalCoroutinesApi
    @Test
    fun `should use database and fetcher`() {

        runBlocking {
            val remoteList = listOf(
                Author(1, "remote author1"),
                Author(2, "remote author2"),
            )

            coEvery { mockAuthorApi.getAll() } answers { remoteList }

            val daoList = listOf(
                Author(1, "local author1"),
            )

            dao.insertAll(daoList)

            val firstResultLatch = CountDownLatch(1)
            val secondResultLatch = CountDownLatch(1)
            val thirdResultLatch = CountDownLatch(1)

            var results: MutableList<Outcome<List<Author>>>? = null

            var resultCounter = 0
            val job = async(testDispatcherProvider.io()) {
                subject.getAllAuthors()
                    .collect {
                        if (results.isNullOrEmpty()) {
                            results = mutableListOf()
                        }
                        results?.add(it)
                        resultCounter += 1
                        when (resultCounter) {
                            1 -> firstResultLatch.countDown()
                            2 -> secondResultLatch.countDown()
                            3 -> thirdResultLatch.countDown()
                        }
                    }
            }

            firstResultLatch.await()
            assert(results!![0].isSuccess)
            assert(results!![0].value!!.size == daoList.size)

            secondResultLatch.await()
            assert(results!![1].isLoading)

            thirdResultLatch.await()
            assert(results!![2].isSuccess)
            assert(results!![2].value!!.size == remoteList.size)

            job.cancelAndJoin()
        }
    }

    @After
    fun tearDown() {
        db.close()
    }

}


