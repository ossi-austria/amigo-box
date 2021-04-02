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
import org.ossiaustria.lib.domain.api.AlbumShareApi
import org.ossiaustria.lib.domain.database.AlbumDao
import org.ossiaustria.lib.domain.database.AlbumShareDao
import org.ossiaustria.lib.domain.database.AppDatabaseImpl
import org.ossiaustria.lib.domain.database.entities.AlbumEntity
import org.ossiaustria.lib.domain.database.entities.AlbumShareEntity
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.AlbumShare
import org.robolectric.RobolectricTestRunner
import timber.log.Timber
import java.util.*

@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
internal class AlbumShareRepositoryTest : AbstractRepositoryTest<AlbumShareEntity, AlbumShare>() {

    lateinit var subject: AlbumShareRepository

    lateinit var albumShareDao: AlbumShareDao
    lateinit var albumDao: AlbumDao

    @RelaxedMockK
    lateinit var albumShareApi: AlbumShareApi

    @Before
    fun before() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, AppDatabaseImpl::class.java).build()
        albumShareDao = db.albumShareDao()
        albumDao = db.albumDao()
        MockKAnnotations.init(this, relaxUnitFun = true)

        subject = AlbumShareRepositoryImpl(albumShareApi, albumShareDao, testDispatcherProvider)

        Timber.plant(Timber.DebugTree())
    }

    @InternalCoroutinesApi
    @Test
    fun `should use database and fetcher`() {

        runBlocking {
            val personId = UUID.randomUUID()

            val id1 = UUID.randomUUID()
            val id2 = UUID.randomUUID()

            val albumId = UUID.randomUUID()
            val album = Album(
                id = albumId,
                ownerId = personId,
                name = "name",
                items = listOf(),
            )
            val remoteList = listOf(
                AlbumShare(id1, senderId = personId, receiverId = personId, album = album),
                AlbumShare(id2, senderId = personId, receiverId = personId, album = album),
            )

            coEvery { albumShareApi.getAll() } answers { remoteList }


            albumDao.insertAll(
                listOf(
                    AlbumEntity(albumId, personId, "name"),
                )
            )
            val daoList = listOf(
                AlbumShareEntity(
                    id1,
                    senderId = personId,
                    receiverId = personId,
                    albumId = album.id
                ),
            )
            albumShareDao.insertAll(daoList)

            testAllStates(daoList, remoteList, subject.getAllAlbumShares())
        }
    }

}
