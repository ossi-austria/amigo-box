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
import org.ossiaustria.lib.domain.api.NfcTagApi
import org.ossiaustria.lib.domain.database.AppDatabaseImpl
import org.ossiaustria.lib.domain.database.NfcTagDao
import org.ossiaustria.lib.domain.database.entities.NfcTagEntity
import org.ossiaustria.lib.domain.models.NfcTag
import org.ossiaustria.lib.domain.models.enums.NfcTagType
import org.robolectric.RobolectricTestRunner
import java.util.*

@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
internal class NfcTagRepositoryTest : AbstractRepositoryTest<NfcTagEntity, NfcTag>() {

    lateinit var subject: NfcTagRepository

    lateinit var nfcTagDao: NfcTagDao

    @RelaxedMockK
    lateinit var nfcTagApi: NfcTagApi

    @Before
    fun before() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, AppDatabaseImpl::class.java).build()
        nfcTagDao = db.nfcTagDao()
        MockKAnnotations.init(this, relaxUnitFun = true)

        subject = NfcTagRepositoryImpl(nfcTagApi, nfcTagDao, testDispatcherProvider)
    }

    @InternalCoroutinesApi
    @Test
    fun `should use database and fetcher`() {

        runBlocking {
            val personId = UUID.randomUUID()

            val id1 = UUID.randomUUID()
            val id2 = UUID.randomUUID()

            val remoteList = listOf(
                NfcTag(id1, creatorId = personId, type = NfcTagType.COLLECTION),
                NfcTag(id2, creatorId = personId, type = NfcTagType.COLLECTION),
            )

            coEvery { nfcTagApi.getAll() } answers { remoteList }

            val daoList = listOf(
                NfcTagEntity(id1, creatorId = personId, type = NfcTagType.COLLECTION),
            )

            nfcTagDao.insertAll(daoList)

            testAllStates(daoList, remoteList, subject.getAllNfcTags())
        }
    }

}
