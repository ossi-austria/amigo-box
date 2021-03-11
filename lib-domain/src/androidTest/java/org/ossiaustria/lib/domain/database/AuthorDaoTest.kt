@file:Suppress("IllegalIdentifier")

package org.ossiaustria.lib.domain.database

import androidx.test.filters.SmallTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.models.Author

/**
 * Example Test for Daos. Needs instrumentation (must run on device or emulator)
 *
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@SmallTest
class AuthorDaoTest : AbstractDaoTest() {
    private lateinit var authorDao: AuthorDao

    override fun init() {
        authorDao = db.authorDao()
    }

    @Test
    fun insertAll_persists_the_items() {
        val author = Author(1, "Firstname Lastname")
        runBlocking { authorDao.insertAll(listOf(author)) }
        val byName = runBlocking { authorDao.findAll().first() }
        assertThat(byName[0], equalTo(author))
    }
}