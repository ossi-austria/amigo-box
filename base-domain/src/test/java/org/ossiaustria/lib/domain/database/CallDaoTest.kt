@file:Suppress("IllegalIdentifier")

package org.ossiaustria.lib.domain.database

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.database.entities.CallEntity
import org.ossiaustria.lib.domain.models.enums.CallState
import org.ossiaustria.lib.domain.models.enums.CallType
import org.robolectric.RobolectricTestRunner
import java.util.*
import java.util.UUID.randomUUID

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
            createdAt = Date(),
            sendAt = Date(),
            retrievedAt = Date(),
            senderId = randomUUID(),
            receiverId = randomUUID(),
            callType = CallType.AUDIO,
            callState = CallState.CALLING,
            startedAt = Date(),
            finishedAt = null
        )
    }

    override fun permuteEntity(entity: CallEntity): CallEntity {
        return entity.copy(
            createdAt = Date(),
            sendAt = Date()
        )
    }

    override fun checkEqual(wrapper: CallEntity, entity: CallEntity) {
        MatcherAssert.assertThat(wrapper, CoreMatchers.equalTo(entity))
    }

    override fun checkSameId(wrapper: CallEntity, entity: CallEntity) {
        MatcherAssert.assertThat(wrapper.id, CoreMatchers.equalTo(entity.id))
    }
}