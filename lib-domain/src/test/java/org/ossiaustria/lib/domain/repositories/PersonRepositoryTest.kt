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
import org.ossiaustria.lib.domain.api.PersonApi
import org.ossiaustria.lib.domain.database.AppDatabaseImpl
import org.ossiaustria.lib.domain.database.PersonDao
import org.ossiaustria.lib.domain.database.entities.PersonEntity
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.MemberType
import org.robolectric.RobolectricTestRunner
import java.util.*

@FlowPreview
@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
internal class PersonRepositoryTest : AbstractRepositoryTest<PersonEntity, Person>() {

    lateinit var subject: PersonRepository
    lateinit var personDao: PersonDao

    @RelaxedMockK
    lateinit var personApi: PersonApi

    @Before
    fun before() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, AppDatabaseImpl::class.java).build()
        personDao = db.personDao()
        MockKAnnotations.init(this, relaxUnitFun = true)

        subject = PersonRepositoryImpl(personApi, personDao, testDispatcherProvider)
    }

    @Test
    fun `should use database and fetcher`() {

        runBlocking {
            val personId1 = UUID.randomUUID()
            val personId2 = UUID.randomUUID()


            val remoteList = listOf(
                mockPerson(personId1, MemberType.MEMBER),
                mockPerson(personId2, MemberType.ANALOGUE),
            )

            coEvery { personApi.getAll() } answers { remoteList }

            val daoList = listOf(
                PersonEntity(
                    personId1,
                    "personId1",
                    "email",
                    UUID.randomUUID(),
                    MemberType.MEMBER
                ),
            )

            personDao.insertAll(daoList)

            testAllStates(daoList, remoteList, subject.getAllPersons())
        }
    }

    private fun mockPerson(
        personId: UUID,
        memberType: MemberType = MemberType.MEMBER
    ): Person {
        return Person(
            id = personId,
            name = "name",
            email = "email",
            memberType = memberType,
            groupId = UUID.randomUUID()
        )
    }

}
