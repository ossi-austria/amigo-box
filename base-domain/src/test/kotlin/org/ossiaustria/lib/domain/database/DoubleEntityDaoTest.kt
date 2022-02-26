@file:Suppress("IllegalIdentifier")

package org.ossiaustria.lib.domain.database

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.database.entities.AbstractEntity
import org.robolectric.RobolectricTestRunner
import java.util.*

/**
 * Example Test for Daos. Needs instrumentation (must run on device or emulator)
 *
 */
@RunWith(RobolectricTestRunner::class)
internal abstract class DoubleEntityDaoTest<ENTITY : AbstractEntity, WRAPPER, DAO : AbstractEntityDao<ENTITY, WRAPPER>> :
    RobolectricDaoTest() {
    protected lateinit var dao: DAO

    abstract fun createEntity(id: UUID = UUID.randomUUID()): ENTITY
    abstract fun permuteEntity(entity: ENTITY): ENTITY
    abstract fun findById(entity: ENTITY): WRAPPER
    abstract fun deleteById(entity: ENTITY)

    abstract fun checkEqual(wrapper: WRAPPER, entity: ENTITY)
    abstract fun checkSameId(wrapper: WRAPPER, entity: ENTITY)


    @DisplayName("insert should persist all items")
    @Test
    fun `insert should persist the item`() {
        val entity = createEntity()

        runBlocking { dao.insert(entity) }

        val findAll = runBlocking { dao.findAll().take(1).first() }

        assertThat(findAll, not(equalTo(listOf())))
        assertThat(findAll.size, equalTo(1))
    }

    @Test
    fun `insertAll should persist all items`() {

        val entity1 = createEntity()
        val entity2 = createEntity()

        runBlocking { dao.insertAll(listOf(entity1, entity2)) }

        val findAll = runBlocking { dao.findAll().take(1).first() }

        assertThat(findAll, not(equalTo(listOf())))
        assertThat(findAll.size, equalTo(2))
        checkEqual(findAll[0], entity1)
        checkEqual(findAll[1], entity2)
    }

    @Test
    fun `insertAll should overwrite items`() {
        val entity1 = createEntity()
        val entity2 = createEntity()

        runBlocking { dao.insertAll(listOf(entity1, entity2)) }

        val entity1b = permuteEntity(entity1)
        val entity3 = createEntity()
        runBlocking { dao.insertAll(listOf(entity1b, entity3)) }

        val findAll = runBlocking { dao.findAll().take(1).first() }

        assertThat(findAll, not(equalTo(listOf())))
        assertThat(findAll.size, equalTo(3))

        val findById = findById(entity1)
        checkEqual(findById, entity1b)
    }

    @Test
    fun `insert should overwrite item`() {
        val entity1 = createEntity()
        val entity2 = createEntity()

        runBlocking { dao.insertAll(listOf(entity1, entity2)) }

        val entity1b = permuteEntity(entity1)
        runBlocking { dao.insert(entity1b) }

        val findAll = runBlocking { dao.findAll().take(1).first() }

        assertThat(findAll, not(equalTo(listOf())))
        assertThat(findAll.size, equalTo(2))

        val findById = findById(entity1)
        checkSameId(findById, entity1)
    }

    @Test
    fun `findAll should load all items`() {
        val mocks = createEntityAndMembers()

        val findAll = runBlocking { dao.findAll().take(1).first() }

        val subject = findAll[0]
        assertThat(subject, not(nullValue()))
        assertThat(findAll.size, equalTo(mocks.size))
    }

    @Test
    fun `findById should load only matching item`() {
        val mocks = createEntityAndMembers()
        val needle = mocks.last()

        val findById = findById(needle)
        checkSameId(findById, needle)
        checkEqual(findById, needle)
    }

    @Test
    fun `deleteAll should keep no items left`() {
        val mocks = createEntityAndMembers()

        runBlocking { dao.insertAll(mocks) }

        val findAll = runBlocking { dao.findAll().take(1).first() }
        assertThat(findAll.size, equalTo(5))

        runBlocking { dao.deleteAll() }

        val findAll2 = runBlocking { dao.findAll().take(1).first() }
        assertThat(findAll2.size, equalTo(0))
    }

    @Test
    fun `deleteById should remove just specific item`() {
        val mocks = createEntityAndMembers()
        val needle = mocks.last()

        runBlocking { dao.insertAll(mocks) }

        val findAll = runBlocking { dao.findAll().take(1).first() }
        assertThat(findAll.size, equalTo(5))

        deleteById(needle)

        val findAll2 = runBlocking { dao.findAll().take(1).first() }
        assertThat(findAll2.size, equalTo(4))

        findAll2.forEach {
            Assert.assertTrue(it != needle)
        }
    }

    @Test
    fun `delete should remove just specific item`() {
        val mocks = createEntityAndMembers()
        val needle = mocks.last()

        runBlocking { dao.insertAll(mocks) }

        val findAll = runBlocking { dao.findAll().take(1).first() }
        assertThat(findAll.size, equalTo(5))

        runBlocking { dao.delete(needle) }

        val findAll2 = runBlocking { dao.findAll().take(1).first() }
        assertThat(findAll2.size, equalTo(4))

        findAll2.forEach {
            Assert.assertTrue(it != needle)
        }
    }

    /**
     * Helper method to generate some entities
     */
    private fun createEntityAndMembers(size: Int = 5): List<ENTITY> {
        val list = (1..size).map { createEntity() }
        runBlocking {
            list.forEach { dao.insert(it) }
        }
        return list
    }
}