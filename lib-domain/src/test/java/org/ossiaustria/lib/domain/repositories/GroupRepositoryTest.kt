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
import org.ossiaustria.lib.domain.api.GroupApi
import org.ossiaustria.lib.domain.common.Outcome
import org.ossiaustria.lib.domain.database.AppDatabaseImpl
import org.ossiaustria.lib.domain.database.GroupDao
import org.ossiaustria.lib.domain.database.PersonDao
import org.ossiaustria.lib.domain.database.entities.GroupEntity
import org.ossiaustria.lib.domain.models.Group
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.MembershipType
import org.robolectric.RobolectricTestRunner
import java.util.*
import java.util.concurrent.CountDownLatch

@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
internal class GroupRepositoryTest {


    lateinit var subject: GroupRepository
    lateinit var groupDao: GroupDao
    lateinit var personDao: PersonDao
    lateinit var db: AppDatabaseImpl


    val dispatcher = TestCoroutineDispatcher()
    private val testDispatcherProvider = TestDispatcherProvider(dispatcher)

    @RelaxedMockK
    lateinit var groupApi: GroupApi

    @Before
    fun before() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, AppDatabaseImpl::class.java).build()
        groupDao = db.groupDao()
        personDao = db.personDao()
        MockKAnnotations.init(this, relaxUnitFun = true)

        subject = GroupRepositoryImpl(groupApi, groupDao, personDao, testDispatcherProvider)
    }

    @InternalCoroutinesApi
    @Test
    fun `should use database and fetcher`() {

        runBlocking {
            val groupId1 = UUID.randomUUID()
            val groupId2 = UUID.randomUUID()

            val centerPerson = UUID.randomUUID()
            val person2 = UUID.randomUUID()
            val person3 = UUID.randomUUID()

            val remoteList = listOf(
                Group(
                    groupId1, "groupId1", listOf(
                        mockPerson(centerPerson, groupId1, MembershipType.CENTER),
                        mockPerson(person2, groupId1, MembershipType.MEMBER),
                    )
                ),
                Group(
                    groupId2, "groupId2",
                    listOf(
                        mockPerson(person2, groupId2, MembershipType.ADMIN),
                        mockPerson(person3, groupId2, MembershipType.MEMBER),
                    )
                ),
            )

            coEvery { groupApi.getAll() } answers { remoteList }

            val daoList = listOf(
                GroupEntity(groupId1, "groupId1"),
            )

            groupDao.insertAll(daoList)

            val firstResultLatch = CountDownLatch(1)
            val secondResultLatch = CountDownLatch(1)
            val thirdResultLatch = CountDownLatch(1)

            var results: MutableList<Outcome<List<Group>>>? = null

            var resultCounter = 0
            val job = async(testDispatcherProvider.io()) {
                subject.getAllGroups()
                    .collect { outcome: Outcome<List<Group>> ->
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

    private fun mockPerson(
        personId: UUID,
        groupId: UUID,
        memberType: MembershipType = MembershipType.MEMBER
    ): Person {
        return Person(
            id = personId,
            name = "name",
            email = "email",
            memberType = memberType,
            groupId = groupId
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

}
