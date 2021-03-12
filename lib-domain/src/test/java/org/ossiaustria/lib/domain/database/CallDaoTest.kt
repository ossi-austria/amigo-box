@file:Suppress("IllegalIdentifier")

package org.ossiaustria.lib.domain.database

import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.database.entities.CallEntity
import org.ossiaustria.lib.domain.models.enums.CallType
import org.robolectric.RobolectricTestRunner
import java.util.*

/**
 * Example Test for Daos. Needs instrumentation (must run on device or emulator)
 *
 */
@RunWith(RobolectricTestRunner::class)
internal class CallDaoTest : SendableDaoTest<CallEntity, CallDao>() {

    override fun init() {
        dao = db.callDao()
    }

    override fun createEntity(id: UUID): CallEntity {
        return CallEntity(
            id = id,
            createdAt = 1, sendAt = 2, retrievedAt = 3,
            senderId = UUID.randomUUID(), receiverId = UUID.randomUUID(),
            callType = CallType.AUDIO,
            startedAt = 5, finishedAt = 6
        )
    }
}