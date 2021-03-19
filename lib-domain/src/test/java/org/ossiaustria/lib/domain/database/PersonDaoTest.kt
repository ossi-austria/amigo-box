@file:Suppress("IllegalIdentifier")

package org.ossiaustria.lib.domain.database

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.database.entities.PersonEntity
import org.ossiaustria.lib.domain.models.enums.MembershipType
import org.robolectric.RobolectricTestRunner
import java.util.*


@RunWith(RobolectricTestRunner::class)
internal class PersonDaoTest : DoubleEntityDaoTest<PersonEntity, PersonEntity, PersonDao>() {

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

    override fun permuteEntity(entity: PersonEntity): PersonEntity {
        return entity.copy(
            name = "new name",
            email = "new email"
        )
    }

    override fun findById(entity: PersonEntity): PersonEntity {
        return runBlocking { dao.findById(entity.personId).take(1).first() }
    }

    override fun checkEqual(wrapper: PersonEntity, entity: PersonEntity) {
        MatcherAssert.assertThat(wrapper, CoreMatchers.equalTo(entity))
    }

    override fun checkSameId(wrapper: PersonEntity, entity: PersonEntity) {
        MatcherAssert.assertThat(wrapper.personId, CoreMatchers.equalTo(entity.personId))
    }

    override fun deleteById(entity: PersonEntity) {
        runBlocking { dao.deleteById(entity.personId) }
    }
}