package org.ossiaustria.lib.domain.repositories

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.api.AlbumApi
import org.ossiaustria.lib.domain.database.AlbumDao
import org.ossiaustria.lib.domain.database.AppDatabaseImpl
import org.ossiaustria.lib.domain.database.MultimediaDao
import org.ossiaustria.lib.domain.database.entities.AlbumEntity
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.Multimedia
import org.ossiaustria.lib.domain.models.enums.MultimediaType
import org.robolectric.RobolectricTestRunner
import java.util.*

@FlowPreview
@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
internal class AlbumRepositoryTest : AbstractRepositoryTest<AlbumEntity, Album>() {

    lateinit var subject: AlbumRepository
    lateinit var albumDao: AlbumDao
    lateinit var multimediaDao: MultimediaDao

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

            coEvery { mockAlbumApi.getShared() } answers { remoteList }

            val daoList = listOf(
                AlbumEntity(id1, UUID.randomUUID(), "name"),
            )

            albumDao.insertAll(daoList)

            testAllStates(daoList, remoteList, subject.getAllAlbums())
        }
    }

    private fun mockMultimedia(personId: UUID): Multimedia {
        return Multimedia(
            UUID.randomUUID(),
            ownerId = personId,
            type = MultimediaType.VIDEO,
            contentType = "localUrl",
            filename = "remoteUrl"
        )
    }
}
