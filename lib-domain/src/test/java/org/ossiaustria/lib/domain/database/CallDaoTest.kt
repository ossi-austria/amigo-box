@file:Suppress("IllegalIdentifier")

package org.ossiaustria.lib.domain.database

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
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
internal class CallDaoTest : SendableDaoTest<CallEntity, CallEntity, CallDao>() {

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

    override fun permuteEntity(entity: CallEntity): CallEntity {
        return entity.copy(
            createdAt = 101,
            sendAt = 102
        )
    }

    override fun checkEqual(wrapper: CallEntity, entity: CallEntity) {
        MatcherAssert.assertThat(wrapper, CoreMatchers.equalTo(entity))
    }

    override fun checkSameId(wrapper: CallEntity, entity: CallEntity) {
        MatcherAssert.assertThat(wrapper.id, CoreMatchers.equalTo(entity.id))
    }
}