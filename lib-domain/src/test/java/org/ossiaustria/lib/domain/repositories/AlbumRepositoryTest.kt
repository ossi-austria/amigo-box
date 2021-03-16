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
import org.ossiaustria.lib.domain.api.AlbumApi
import org.ossiaustria.lib.domain.common.Outcome
import org.ossiaustria.lib.domain.database.AlbumDao
import org.ossiaustria.lib.domain.database.AppDatabaseImpl
import org.ossiaustria.lib.domain.database.MultimediaDao
import org.ossiaustria.lib.domain.database.entities.AlbumEntity
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.Multimedia
import org.ossiaustria.lib.domain.models.enums.MultimediaType
import org.robolectric.RobolectricTestRunner
import java.util.*
import java.util.concurrent.CountDownLatch

@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
internal class AlbumRepositoryTest {


    lateinit var subject: AlbumRepository
    lateinit var albumDao: AlbumDao
    lateinit var multimediaDao: MultimediaDao
    lateinit var db: AppDatabaseImpl


    val dispatcher = TestCoroutineDispatcher()
    private val testDispatcherProvider = TestDispatcherProvider(dispatcher)

    @RelaxedMockK
    lateinit var mockAlbumApi: AlbumApi

    @Before
    fun before() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, AppDatabaseImpl::class.java).build()
        albumDao = db.albumDao()
        multimediaDao = db.multimediaDao()
        MockKAnnotations.init(this, relaxUnitFun = true)

        subject = AlbumRepositoryImpl(mockAlbumApi, albumDao, multimediaDao, testDispatcherProvider)
    }

    @InternalCoroutinesApi
    @Test
    fun `should use database and fetcher`() {

        runBlocking {
            val personId = UUID.randomUUID()

            val id1 = UUID.randomUUID()
            val id2 = UUID.randomUUID()

            val remoteList = listOf(
                Album(id1, "album1", personId, listOf()),
                Album(
                    id2, "album2", personId,
                    listOf(
                        mockMultimedia(personId),
                        mockMultimedia(personId),
                    )
                ),
            )

            coEvery { mockAlbumApi.getAll() } answers { remoteList }

            val daoList = listOf(
                AlbumEntity(id1, UUID.randomUUID(), "name"),
            )

            albumDao.insertAll(daoList)

            val firstResultLatch = CountDownLatch(1)
            val secondResultLatch = CountDownLatch(1)
            val thirdResultLatch = CountDownLatch(1)

            var results: MutableList<Outcome<List<Album>>>? = null

            var resultCounter = 0
            val job = async(testDispatcherProvider.io()) {
                subject.getAllAlbums()
                    .collect { outcome: Outcome<List<Album>> ->
                        if (results.isNullOrEmpty()) {
                            results = mutableListOf()
                        }
                        results?.add(outcome)
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

    private fun mockMultimedia(personId: UUID): Multimedia {
        return Multimedia(
            UUID.randomUUID(),
            senderId = personId,
            receiverId = personId,
            ownerId = personId,
            type = MultimediaType.VIDEO,
            localUrl = "localUrl",
            remoteUrl = "remoteUrl"
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

}
