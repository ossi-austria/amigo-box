@file:Suppress("IllegalIdentifier")

package org.ossiaustria.lib.domain.database

import androidx.test.filters.SmallTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.database.entities.GroupEntity
import java.util.*

/**
 * Example Test for Daos. Needs instrumentation (must run on device or emulator)
 *
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@SmallTest
class GroupDaoTest : AbstractDaoTest() {
    private lateinit var groupDao: GroupDao

    override fun init() {
        groupDao = db.groupDao()
    }

    @Test
    fun insertAll_persists_the_items() {
        val id = UUID.randomUUID()
        val group = GroupEntity(id, "Firstname Lastname", null)
        runBlocking { groupDao.insertAll(listOf(group)) }
        val findAll = runBlocking { groupDao.findAll() }
        assertThat(findAll, not(equalTo(listOf())))
        val groupWithMembershipsAndPersons = findAll[0]
        assertThat(groupWithMembershipsAndPersons.group.groupId, equalTo(group.groupId))
        assertThat(groupWithMembershipsAndPersons.group, equalTo(group))
    }
}