@file:Suppress("IllegalIdentifier")

package org.ossiaustria.lib.domain.database

import androidx.test.filters.SmallTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.database.entities.GroupEntity
import org.ossiaustria.lib.domain.database.entities.MembershipEntity
import org.ossiaustria.lib.domain.database.entities.MembershipType
import org.ossiaustria.lib.domain.database.entities.PersonEntity
import java.util.*

/**
 * Example Test for Daos. Needs instrumentation (must run on device or emulator)
 *
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@SmallTest
class GroupDaoTest : AbstractDaoTest() {
    private lateinit var groupDao: GroupDao
    private lateinit var personDao: PersonDao

    override fun init() {
        groupDao = db.groupDao()
        personDao = db.personDao()
    }

    @DisplayName("insert should persist all items")
    @Test
    fun insert_should_persist_the_items() {
        val id = UUID.randomUUID()
        val group = GroupEntity(id, "Firstname Lastname", null)

        runBlocking { groupDao.insert(group) }

        val findAll = runBlocking { groupDao.findAll() }

        assertThat(findAll, not(equalTo(listOf())))
        assertThat(findAll.size, equalTo(1))
        assertThat(findAll[0].group.groupId, equalTo(group.groupId))
        assertThat(findAll[0].group, equalTo(group))
    }

    @Test
    fun insert_should_persist_all_items() {
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()
        val group1 = GroupEntity(id1, "Firstname Lastname", null)
        val group2 = GroupEntity(id2, "Firstname Lastname", null)

        runBlocking { groupDao.insertAll(listOf(group1, group2)) }

        val findAll = runBlocking { groupDao.findAll() }

        assertThat(findAll, not(equalTo(listOf())))
        assertThat(findAll.size, equalTo(2))
        assertThat(findAll[0].group, equalTo(group1))
        assertThat(findAll[1].group, equalTo(group2))
    }

    @Test
    fun insertAll_should_not_overwrite_items() {
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()
        val group1 = GroupEntity(id1, "Firstname Lastname", null)
        val group2 = GroupEntity(id2, "Firstname Lastname", null)

        runBlocking { groupDao.insertAll(listOf(group1, group2)) }

        val group1b = GroupEntity(id1, "new name", null)
        val group3 = GroupEntity(UUID.randomUUID(), "Firstname Lastname", null)
        runBlocking { groupDao.insertAll(listOf(group1b, group3)) }

        val findAll = runBlocking { groupDao.findAll() }

        assertThat(findAll, not(equalTo(listOf())))
        assertThat(findAll.size, equalTo(3))
        assertThat(findAll[0].group, equalTo(group1))
        assertThat(findAll[1].group, equalTo(group2))
        assertThat(findAll[2].group, equalTo(group3))
    }

    @Test
    fun insert_should_overwrite_items() {
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()
        val group1 = GroupEntity(id1, "Firstname Lastname", null)
        val group2 = GroupEntity(id2, "Firstname Lastname", null)

        runBlocking { groupDao.insertAll(listOf(group1, group2)) }

        val group1b = GroupEntity(id1, "new name", null)
        val group3 = GroupEntity(UUID.randomUUID(), "Firstname Lastname", null)
        runBlocking { groupDao.insertAll(listOf(group1b, group3)) }
        runBlocking { groupDao.insert(group1b) }

        val findAll = runBlocking { groupDao.findAll() }

        assertThat(findAll, not(equalTo(listOf())))
        assertThat(findAll.size, equalTo(3))
        assertThat(findAll[0].group, equalTo(group1b))
        assertThat(findAll[1].group, equalTo(group2))
        assertThat(findAll[2].group, equalTo(group3))
    }

    @Test
    fun findAll_should_load_group() {
        createGroupAndMembers()

        val findAll = runBlocking { groupDao.findAll() }

        val subject = findAll[0]
        assertThat(subject.group, not(nullValue()))
        assertThat(subject.group.groupId, not(nullValue()))
        assertThat(subject.group.name, not(nullValue()))
        assertThat(subject.group.centerPersonId, not(nullValue()))
    }

    @Test
    fun findAll_should_load_members() {
        createGroupAndMembers()

        val findAll = runBlocking { groupDao.findAll() }

        val subject = findAll[0]
        assertThat(subject.members, not(nullValue()))
        assertThat(subject.members.size, equalTo(2))
        assertThat(subject.members[0], not(nullValue()))
        assertThat(subject.members[0].membershipEntity, not(nullValue()))
        assertThat(subject.members[0].personEntity, not(nullValue()))
        assertThat(subject.members[1], not(nullValue()))

    }

    private fun createGroupAndMembers() {
        val groupId = UUID.randomUUID()
        val centerPersonId = UUID.randomUUID()
        val memberId = UUID.randomUUID()
        val adminId = UUID.randomUUID()
        val group1 = GroupEntity(groupId, "Firstname Lastname", centerPersonId)
        val centerPerson = PersonEntity(centerPersonId, "center", "email")
        val member = PersonEntity(memberId, "member", "email")
        val admin = PersonEntity(adminId, "admin", "email")

        val membership1 =
            MembershipEntity(UUID.randomUUID(), groupId, centerPersonId, MembershipType.CENTER)
        val membership2 =
            MembershipEntity(UUID.randomUUID(), groupId, memberId, MembershipType.MEMBER)
        val membership3 =
            MembershipEntity(UUID.randomUUID(), groupId, adminId, MembershipType.ADMIN)


        runBlocking {
            groupDao.insertAll(listOf(group1))
            personDao.insert(centerPerson)
            personDao.insert(member)
            personDao.insert(admin)
            groupDao.insert(membership1)
            groupDao.insert(membership2)
            groupDao.insert(membership3)

        }
    }
}