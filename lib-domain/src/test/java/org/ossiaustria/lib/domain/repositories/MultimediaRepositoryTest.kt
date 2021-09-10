package org.ossiaustria.lib.domain.repositories

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.api.MultimediaApi
import org.ossiaustria.lib.domain.database.AppDatabaseImpl
import org.ossiaustria.lib.domain.database.MultimediaDao
import org.ossiaustria.lib.domain.database.entities.MultimediaEntity
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.Multimedia
import org.ossiaustria.lib.domain.models.enums.MultimediaType
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config
internal class MultimediaRepositoryTest : AbstractRepositoryTest<MultimediaEntity, Multimedia>() {

    lateinit var subject: MultimediaRepository

    lateinit var multimediaDao: MultimediaDao

    @RelaxedMockK
    lateinit var multimediaApi: MultimediaApi

    @Before
    fun before() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, AppDatabaseImpl::class.java).build()
        multimediaDao = db.multimediaDao()
        MockKAnnotations.init(this, relaxUnitFun = true)

        subject = MultimediaRepositoryImpl(multimediaApi, multimediaDao, testDispatcherProvider)
    }

    @InternalCoroutinesApi
    @Test
    fun `should use database and fetcher`() {

        runBlocking {
            val personId = UUID.randomUUID()

            val id1 = UUID.randomUUID()
            val id2 = UUID.randomUUID()

            val album = Album(
                id = UUID.randomUUID(),
                ownerId = UUID.randomUUID(),
                name = "name",
                items = listOf(),
            )
            val remoteList = listOf(
                Multimedia(
                    id1, senderId = personId, receiverId = personId, type = MultimediaType.VIDEO,
                    albumId = album.id,
                    localUrl = "localUrl",
                    remoteUrl = "remoteUrl",
                    ownerId = personId
                ),
                Multimedia(
                    id2, senderId = personId, receiverId = personId, type = MultimediaType.VIDEO,
                    albumId = album.id,
                    localUrl = "localUrl",
                    remoteUrl = "remoteUrl",
                    ownerId = personId
                ),
            )

            coEvery { multimediaApi.getAll() } answers { remoteList }

            val daoList = listOf(
                MultimediaEntity(
                    id1,
                    senderId = personId,
                    receiverId = personId,
                    ownerId = personId,
                    type = MultimediaType.VIDEO,
                    albumId = album.id,
                    localUrl = "localUrl",
                    remoteUrl = "remoteUrl",
                ),
            )

            multimediaDao.insertAll(daoList)

            testAllStates(daoList, remoteList, subject.getAllMultimedias())
        }
    }

}
