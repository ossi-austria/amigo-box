@file:Suppress("IllegalIdentifier")

package org.ossiaustria.lib.domain.database

import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.database.entities.PersonEntity
import org.ossiaustria.lib.domain.models.enums.MembershipType
import org.robolectric.RobolectricTestRunner
import java.util.*


@RunWith(RobolectricTestRunner::class)
internal class PersonDaoTest : SimpleEntityDaoTest<PersonEntity, PersonDao>() {

    override fun init() {
        dao = db.personDao()
    }

    override fun createEntity(id: UUID): PersonEntity {
        return PersonEntity(
            personId = UUID.randomUUID(),
            memberType = MembershipType.MEMBER,
            email = "email",
            name = "name",
            groupId = UUID.randomUUID()
        )
    }

    override fun permutateEntity(entity: PersonEntity): PersonEntity {
        return entity.copy(
            name = "new name",
            email = "new email"
        )
    }

    override fun findBy(entity: PersonEntity): PersonEntity {
        return runBlocking { dao.findById(entity.personId) }
    }
}