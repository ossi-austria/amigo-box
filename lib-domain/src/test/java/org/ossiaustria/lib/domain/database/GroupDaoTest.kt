@file:Suppress("IllegalIdentifier")

package org.ossiaustria.lib.domain.database

import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.database.entities.GroupEntity
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
class GroupDaoTest : RobolectricDaoTest() {
    private lateinit var groupDao: GroupDao
    private lateinit var personDao: PersonDao

    override fun init() {
        groupDao = db.groupDao()
        personDao = db.personDao()
    }


    @DisplayName("insert should persist all items")
    @Test
    fun `insert should persist the item`() {
        val id = UUID.randomUUID()
        val group = GroupEntity(id, "Firstname Lastname")

        runBlocking { groupDao.insert(group) }

        val findAll = runBlocking { groupDao.findAll() }

        assertThat(findAll, not(equalTo(listOf())))
        assertThat(findAll.size, equalTo(1))
        assertThat(findAll[0].group.groupId, equalTo(group.groupId))
        assertThat(findAll[0].group, equalTo(group))
    }

    @Test
    fun `insertAll should persist all items`() {
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()
        val group1 = GroupEntity(id1, "Firstname Lastname")
        val group2 = GroupEntity(id2, "Firstname Lastname")

        runBlocking { groupDao.insertAll(listOf(group1, group2)) }

        val findAll = runBlocking { groupDao.findAll() }

        assertThat(findAll, not(equalTo(listOf())))
        assertThat(findAll.size, equalTo(2))
        assertThat(findAll[0].group, equalTo(group1))
        assertThat(findAll[1].group, equalTo(group2))
    }

    @Test
    fun `insertAll should not overwrite items`() {
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()
        val group1 = GroupEntity(id1, "1")
        val group2 = GroupEntity(id2, "2")

        runBlocking { groupDao.insertAll(listOf(group1, group2)) }

        val group1b = GroupEntity(id1, "1b")
        val group3 = GroupEntity(UUID.randomUUID(), "3")
        runBlocking { groupDao.insertAll(listOf(group1b, group3)) }

        val findAll = runBlocking { groupDao.findAll() }

        assertThat(findAll, not(equalTo(listOf())))
        assertThat(findAll.size, equalTo(3))

        val findById = runBlocking { groupDao.findById(id1) }
        assertThat(findById.group, equalTo(group1))
    }

    @Test
    fun `insert should overwrite items`() {
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()
        val group1 = GroupEntity(id1, "1")
        val group2 = GroupEntity(id2, "2")

        runBlocking { groupDao.insertAll(listOf(group1, group2)) }

        val group1b = GroupEntity(id1, "new name")
        runBlocking { groupDao.insert(group1b) }

        val findAll = runBlocking { groupDao.findAll() }

        assertThat(findAll, not(equalTo(listOf())))
        assertThat(findAll.size, equalTo(2))

        val findById = runBlocking { groupDao.findById(id1) }
        assertThat(findById.group, equalTo(group1b))
    }

    @Test
    fun `findAll should load group`() {
        createGroupAndMembers()

        val findAll = runBlocking { groupDao.findAll() }

        val subject = findAll[0]
        assertThat(subject.group, not(nullValue()))
        assertThat(subject.group.groupId, not(nullValue()))
        assertThat(subject.group.name, not(nullValue()))
    }

    @Test
    fun `findAll should load members`() {
        createGroupAndMembers()

        val findAll = runBlocking { groupDao.findAll() }

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

        val subject = runBlocking { groupDao.findById(group.groupId) }

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

        val findAll = runBlocking { groupDao.findAll() }
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

        val findAll = runBlocking { groupDao.findAll() }
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

        val findAll = runBlocking { groupDao.findAll() }
        val subject = findAll[0].toGroup()

        assertThat(subject.members, not(nullValue()))
        assertThat(subject.members.size, equalTo(3))
    }

    @Test
    fun `mapping should map centerPerson type`() {
        createGroupAndMembers()

        val findAll = runBlocking { groupDao.findAll() }
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
            groupDao.insert(group1)
            personDao.insert(centerPerson)
            personDao.insert(member)
            personDao.insert(admin)
        }
        return group1
    }
}