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
import org.ossiaustria.lib.domain.api.CallApi
import org.ossiaustria.lib.domain.database.AppDatabaseImpl
import org.ossiaustria.lib.domain.database.CallDao
import org.ossiaustria.lib.domain.database.entities.CallEntity
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.enums.CallType
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config
internal class CallRepositoryTest : AbstractRepositoryTest<CallEntity, Call>() {

    lateinit var subject: CallRepository

    lateinit var callDao: CallDao

    @RelaxedMockK
    lateinit var callApi: CallApi

    @Before
    fun before() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, AppDatabaseImpl::class.java).build()
        callDao = db.callDao()
        MockKAnnotations.init(this, relaxUnitFun = true)

        subject = CallRepositoryImpl(callApi, callDao, testDispatcherProvider)
    }

    @InternalCoroutinesApi
    @Test
    fun `should use database and fetcher`() {

        runBlocking {
            val personId = UUID.randomUUID()

            val id1 = UUID.randomUUID()
            val id2 = UUID.randomUUID()

            val remoteList = listOf(
                Call(
                    id1,
                    senderId = personId,
                    receiverId = personId,
                    callType = CallType.VIDEO,
                    startedAt = 0,
                    finishedAt = 1
                ),
                Call(
                    id2,
                    senderId = personId,
                    receiverId = personId,
                    callType = CallType.VIDEO,
                    startedAt = 0,
                    finishedAt = 1
                ),
            )

            coEvery { callApi.getAll() } answers { remoteList }

            val daoList = listOf(
                CallEntity(
                    id1,
                    senderId = personId,
                    receiverId = personId,
                    callType = CallType.VIDEO,
                    startedAt = 0,
                    finishedAt = 1,
                    createdAt = 1
                ),
            )

            callDao.insertAll(daoList)

            testAllStates(daoList, remoteList, subject.getAllCalls())
        }
    }

}
