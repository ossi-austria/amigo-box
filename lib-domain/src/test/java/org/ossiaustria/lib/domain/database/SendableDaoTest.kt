@file:Suppress("IllegalIdentifier")

package org.ossiaustria.lib.domain.database

import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.database.entities.SendableEntity
import org.robolectric.RobolectricTestRunner
import java.util.*

/**
 * Example Test for Daos. Needs instrumentation (must run on device or emulator)
 *
 */
@RunWith(RobolectricTestRunner::class)
internal abstract class SendableDaoTest<T : SendableEntity, DAO : SendableDao<T>> :
    RobolectricDaoTest() {
    protected lateinit var dao: DAO

    abstract fun createEntity(id: UUID = UUID.randomUUID()): T

    @DisplayName("insert should persist all items")
    @Test
    fun `insert should persist the item`() {
        val entity = createEntity()

        runBlocking { dao.insert(entity) }

        val findAll = runBlocking { dao.findAll() }

        assertThat(findAll, not(equalTo(listOf())))
        assertThat(findAll.size, equalTo(1))
        assertThat(findAll[0].id, equalTo(entity.id))
    }

    @Test
    fun `insertAll should persist all items`() {

        val entity1 = createEntity()
        val entity2 = createEntity()

        runBlocking { dao.insertAll(listOf(entity1, entity2)) }

        val findAll = runBlocking { dao.findAll() }

        assertThat(findAll, not(equalTo(listOf())))
        assertThat(findAll.size, equalTo(2))
        assertThat(findAll[0], equalTo(entity1))
        assertThat(findAll[1], equalTo(entity2))
    }

    @Test
    fun `insertAll should not overwrite items`() {
        val entity1 = createEntity()
        val entity2 = createEntity()

        runBlocking { dao.insertAll(listOf(entity1, entity2)) }

        val entity1b = createEntity(id = entity1.id)
        val entity3 = createEntity()
        runBlocking { dao.insertAll(listOf(entity1b, entity3)) }

        val findAll = runBlocking { dao.findAll() }

        assertThat(findAll, not(equalTo(listOf())))
        assertThat(findAll.size, equalTo(3))

        val findById = runBlocking { dao.findById(entity1.id) }
        assertThat(findById, equalTo(entity1))
    }

    @Test
    fun `insert should overwrite item`() {
        val entity1 = createEntity()
        val entity2 = createEntity()

        runBlocking { dao.insertAll(listOf(entity1, entity2)) }

        val entity1b = createEntity(id = entity1.id)
        runBlocking { dao.insert(entity1b) }

        val findAll = runBlocking { dao.findAll() }

        assertThat(findAll, not(equalTo(listOf())))
        assertThat(findAll.size, equalTo(2))

        val findById = runBlocking { dao.findById(entity1.id) }
        assertThat(findById, equalTo(entity1b))
    }

    @Test
    fun `findAll should load entity`() {
        createEntityAndMembers()

        val findAll = runBlocking { dao.findAll() }

        val subject = findAll[0]
        assertThat(subject, not(nullValue()))
        assertThat(subject.id, not(nullValue()))
        assertThat(subject.retrievedAt, not(nullValue()))
    }

    /**
     *      TODO
     *    TODO add some tests:
     *    - delete 1 should not delete the others
     *    - delete all should delete all
     *    - findById
     *    - findBySender
     *    - findByReceiver

     */

    /**
     * Helper method to generate some entities
     */
    private fun createEntityAndMembers(size: Int = 5): List<T> {
        val list = (1..size).map { createEntity() }
        runBlocking {
            list.forEach { dao.insert(it) }
        }
        return list
    }
}