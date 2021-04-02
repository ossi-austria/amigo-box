@file:Suppress("IllegalIdentifier")

package org.ossiaustria.lib.domain.database

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.database.entities.SendableEntity
import org.robolectric.RobolectricTestRunner

/**
 * Example Test for Daos. Needs instrumentation (must run on device or emulator)
 *
 */
@RunWith(RobolectricTestRunner::class)
internal abstract class SendableDaoTest<ENTITY : SendableEntity, WRAPPER, DAO : SendableDao<ENTITY, WRAPPER>> :
    DoubleEntityDaoTest<ENTITY, WRAPPER, DAO>() {

    /**
     *      TODO
     *    TODO add some tests:
     *
     *    - findBySender
     *    - findByReceiver
     */

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

    override fun findById(entity: ENTITY): WRAPPER {
        return runBlocking { dao.findById(entity.id).take(1).first() }
    }

    override fun deleteById(entity: ENTITY) {
        runBlocking { dao.deleteById(entity.id) }
    }

}