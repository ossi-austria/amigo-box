package org.ossiaustria.lib.domain.repositories

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.api.NfcInfoApi
import org.ossiaustria.lib.domain.database.AppDatabaseImpl
import org.ossiaustria.lib.domain.database.NfcInfoDao
import org.ossiaustria.lib.domain.database.entities.NfcInfoEntity
import org.ossiaustria.lib.domain.models.NfcInfo
import org.ossiaustria.lib.domain.models.enums.NfcTagType
import org.robolectric.RobolectricTestRunner
import java.util.*

@FlowPreview
@RunWith(RobolectricTestRunner::class)
internal class NfcTagRepositoryTest : AbstractRepositoryTest<NfcInfoEntity, NfcInfo>() {

    lateinit var subject: NfcInfoRepository

    lateinit var nfcInfoDao: NfcInfoDao

    @RelaxedMockK
    lateinit var nfcInfoApi: NfcInfoApi

    @Before
    fun before() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, AppDatabaseImpl::class.java).build()
        nfcInfoDao = db.nfcInfoDao()
        MockKAnnotations.init(this, relaxUnitFun = true)

        subject = NfcInfoRepositoryImpl(nfcInfoApi, nfcInfoDao, testDispatcherProvider)
    }

    @Test
    fun `should use database and fetcher`() {

        runBlocking {
            val personId = UUID.randomUUID()

            val id1 = UUID.randomUUID()
            val id2 = UUID.randomUUID()

            val remoteList = listOf(
                NfcInfo(
                    id1,
                    creatorId = personId,
                    type = NfcTagType.OPEN_ALBUM,
                    name = "name",
                    nfcRef = "ref"
                ),
                NfcInfo(
                    id2,
                    creatorId = personId,
                    type = NfcTagType.OPEN_ALBUM,
                    name = "name",
                    nfcRef = "ref"
                ),
            )

            coEvery { nfcInfoApi.getAllAccessibleNfcs() } answers { remoteList }

            val daoList = listOf(
                NfcInfoEntity(
                    id1,
                    creatorId = personId,
                    type = NfcTagType.OPEN_ALBUM,
                    name = "name",
                    nfcRef = "ref",
                ),
            )

            nfcInfoDao.insertAll(daoList)

            testAllStates(daoList, remoteList, subject.getAllNfcTags())
        }
    }

}
