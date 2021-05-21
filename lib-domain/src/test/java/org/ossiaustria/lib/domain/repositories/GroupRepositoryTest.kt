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
import org.ossiaustria.lib.domain.EntityMocks
import org.ossiaustria.lib.domain.api.GroupApi
import org.ossiaustria.lib.domain.database.AppDatabaseImpl
import org.ossiaustria.lib.domain.database.GroupDao
import org.ossiaustria.lib.domain.database.PersonDao
import org.ossiaustria.lib.domain.database.entities.GroupEntity
import org.ossiaustria.lib.domain.models.Group
import org.ossiaustria.lib.domain.models.enums.MembershipType
import org.robolectric.RobolectricTestRunner
import java.util.*

@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
internal class GroupRepositoryTest : AbstractRepositoryTest<GroupEntity, Group>() {

    lateinit var subject: GroupRepository
    lateinit var groupDao: GroupDao
    lateinit var personDao: PersonDao

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
                        EntityMocks.person(centerPerson, groupId1, MembershipType.CENTER),
                        EntityMocks.person(person2, groupId1, MembershipType.MEMBER),
                    )
                ),
                Group(
                    groupId2, "groupId2",
                    listOf(
                        EntityMocks.person(person2, groupId2, MembershipType.ADMIN),
                        EntityMocks.person(person3, groupId2, MembershipType.MEMBER),
                    )
                ),
            )

            coEvery { groupApi.getAll() } answers { remoteList }

            val daoList = listOf(
                GroupEntity(groupId1, "groupId1"),
            )

            groupDao.insertAll(daoList)

            testAllStates(daoList, remoteList, subject.getAllGroups())
        }
    }
}
