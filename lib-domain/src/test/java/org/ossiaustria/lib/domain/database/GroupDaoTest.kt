@file:Suppress("IllegalIdentifier")

package org.ossiaustria.lib.domain.database

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.database.entities.GroupEntity
import org.ossiaustria.lib.domain.database.entities.GroupEntityWithMembers
import org.ossiaustria.lib.domain.database.entities.PersonEntity
import org.ossiaustria.lib.domain.database.entities.toGroup
import org.ossiaustria.lib.domain.models.enums.MembershipType
import org.robolectric.RobolectricTestRunner
import java.util.*

/**
 * Example Test for Daos. Needs instrumentation (must run on device or emulator)
 *
 */
@RunWith(RobolectricTestRunner::class)
internal class GroupDaoTest : DoubleEntityDaoTest<GroupEntity, GroupEntityWithMembers, GroupDao>() {

    private lateinit var personDao: PersonDao

    override fun init() {
        dao = db.groupDao()
        personDao = db.personDao()
    }


    @DisplayName("insert should persist all items")
    @Test
    fun `insert should persist the item and set groupId`() {
        val id = UUID.randomUUID()
        val group = GroupEntity(id, "Firstname Lastname")

        runBlocking { dao.insert(group) }

        val findAll = runBlocking { dao.findAll().take(1).first() }

        assertThat(findAll, not(equalTo(listOf<GroupEntityWithMembers>())))
        assertThat(findAll.size, equalTo(1))
        assertThat(findAll[0].group.groupId, equalTo(group.groupId))
        assertThat(findAll[0].group, equalTo(group))
    }


    @Test
    fun `insertAll should overwrite group items`() {
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()
        val group1 = GroupEntity(id1, "1")
        val group2 = GroupEntity(id2, "2")

        runBlocking { dao.insertAll(listOf(group1, group2)) }

        val group1b = GroupEntity(id1, "1b")
        val group3 = GroupEntity(UUID.randomUUID(), "3")
        runBlocking { dao.insertAll(listOf(group1b, group3)) }

        val findAll = runBlocking { dao.findAll().take(1).first() }

        assertThat(findAll, not(equalTo(listOf())))
        assertThat(findAll.size, equalTo(3))

        val findById = runBlocking { dao.findById(id1).take(1).first() }
        assertThat(findById.group, equalTo(group1b))
    }

    @Test
    fun `insert should overwrite items`() {
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()
        val group1 = GroupEntity(id1, "1")
        val group2 = GroupEntity(id2, "2")

        runBlocking { dao.insertAll(listOf(group1, group2)) }

        val group1b = GroupEntity(id1, "new name")
        runBlocking { dao.insert(group1b) }

        val findAll = runBlocking { dao.findAll().take(1).first() }

        assertThat(findAll, not(equalTo(listOf())))
        assertThat(findAll.size, equalTo(2))

        val findById = runBlocking { dao.findById(id1).take(1).first() }
        assertThat(findById.group, equalTo(group1b))
    }

    @Test
    fun `findAll should load group`() {
        createGroupAndMembers()

        val findAll = runBlocking { dao.findAll().take(1).first() }

        val subject = findAll[0]
        assertThat(subject.group, not(nullValue()))
        assertThat(subject.group.groupId, not(nullValue()))
        assertThat(subject.group.name, not(nullValue()))
    }

    @Test
    fun `findAll should load members`() {
        createGroupAndMembers()

        val findAll = runBlocking { dao.findAll().take(1).first() }

        val subject = findAll[0]
        assertThat(subject.members, not(nullValue()))
        assertThat(subject.members.size, equalTo(3))
        assertThat(subject.members[0], not(nullValue()))
        assertThat(subject.members[0].email, not(nullValue()))
        assertThat(subject.members[0].groupId, not(nullValue()))
        assertThat(subject.members[0].personId, not(nullValue()))
        assertThat(subject.members[0].memberType, not(nullValue()))
        assertThat(subject.members[0].name, not(nullValue()))

    }

    @Test
    fun `find should load members`() {
        val group = createGroupAndMembers()

        val subject = runBlocking { dao.findById(group.groupId).take(1).first() }

        assertThat(subject.members, not(nullValue()))
        assertThat(subject.members.size, equalTo(3))
        assertThat(subject.members[0], not(nullValue()))
        assertThat(subject.members[0].email, not(nullValue()))
        assertThat(subject.members[0].groupId, not(nullValue()))
        assertThat(subject.members[0].personId, not(nullValue()))
        assertThat(subject.members[0].memberType, not(nullValue()))
        assertThat(subject.members[0].name, not(nullValue()))

    }

    @Test
    fun `mapping should contain person fields`() {
        createGroupAndMembers()

        val findAll = runBlocking { dao.findAll().take(1).first() }
        val subject = findAll[0].toGroup()

        assertThat(subject.members, not(nullValue()))
        assertThat(subject.members[0], not(nullValue()))
        assertThat(subject.members[0].email, not(nullValue()))
        assertThat(subject.members[0].memberType, not(nullValue()))
        assertThat(subject.members[0].name, not(nullValue()))

    }

    @Test
    fun `mapping should map admin type`() {
        createGroupAndMembers()

        val findAll = runBlocking { dao.findAll().take(1).first() }
        val subject = findAll[0].toGroup()

        assertThat(subject.members, not(nullValue()))
        assertThat(subject.admins.size, equalTo(1))

        subject.admins.forEach {
            assertThat(
                it.memberType,
                equalTo(MembershipType.ADMIN)
            )
        }
    }

    @Test
    fun `mapping should count all members`() {
        createGroupAndMembers()

        val findAll = runBlocking { dao.findAll().take(1).first() }
        val subject = findAll[0].toGroup()

        assertThat(subject.members, not(nullValue()))
        assertThat(subject.members.size, equalTo(3))
    }

    @Test
    fun `mapping should map centerPerson type`() {
        createGroupAndMembers()

        val findAll = runBlocking { dao.findAll().take(1).first() }
        val subject = findAll[0].toGroup()

        assertThat(subject.centerPerson, not(nullValue()))

        subject.centerPerson.let {
            assertThat(
                it!!.memberType,
                equalTo(MembershipType.CENTER)
            )
        }
    }

    private fun createGroupAndMembers(): GroupEntity {
        val groupId = UUID.randomUUID()
        val centerPersonId = UUID.randomUUID()
        val memberId = UUID.randomUUID()
        val adminId = UUID.randomUUID()
        val group1 = GroupEntity(groupId, "group")
        val centerPerson =
            PersonEntity(centerPersonId, "center", "email", groupId, MembershipType.CENTER)
        val member = PersonEntity(memberId, "member", "email", groupId, MembershipType.MEMBER)
        val admin = PersonEntity(adminId, "admin", "email", groupId, MembershipType.ADMIN)

        runBlocking {
            dao.insert(group1)
            personDao.insert(centerPerson)
            personDao.insert(member)
            personDao.insert(admin)
        }
        return group1
    }

    override fun createEntity(id: UUID): GroupEntity {
        return GroupEntity(
            groupId = UUID.randomUUID(),
            name = "name"
        )
    }

    override fun permuteEntity(entity: GroupEntity): GroupEntity {
        return entity.copy(
            name = "new name",
        )
    }

    override fun findById(entity: GroupEntity): GroupEntityWithMembers {
        return runBlocking { dao.findById(entity.groupId).take(1).first() }
    }

    override fun checkEqual(wrapper: GroupEntityWithMembers, entity: GroupEntity) {
        assertThat(wrapper.group, equalTo(entity))
    }

    override fun checkSameId(wrapper: GroupEntityWithMembers, entity: GroupEntity) {
        assertThat(wrapper.group.groupId, equalTo(entity.groupId))
    }

    override fun deleteById(entity: GroupEntity) {
        runBlocking { dao.deleteById(entity.groupId) }
    }
}