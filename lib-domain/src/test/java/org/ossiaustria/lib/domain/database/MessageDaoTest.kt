@file:Suppress("IllegalIdentifier")

package org.ossiaustria.lib.domain.database

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.database.entities.MessageEntity
import org.robolectric.RobolectricTestRunner
import java.util.*


@RunWith(RobolectricTestRunner::class)
internal class MessageDaoTest : SendableDaoTest<MessageEntity, MessageEntity, MessageDao>() {

    override fun init() {
        dao = db.messageDao()
    }

    override fun createEntity(id: UUID): MessageEntity {
        return MessageEntity(
            id = id,
            createdAt = 1, sendAt = 2, retrievedAt = 3,
            senderId = UUID.randomUUID(), receiverId = UUID.randomUUID(),
            text = UUID.randomUUID().toString()
        )
    }

    override fun permuteEntity(entity: MessageEntity): MessageEntity {
        return entity.copy(
            createdAt = 101,
            sendAt = 102
        )
    }

    override fun checkEqual(wrapper: MessageEntity, entity: MessageEntity) {
        MatcherAssert.assertThat(wrapper, CoreMatchers.equalTo(entity))
    }

    override fun checkSameId(wrapper: MessageEntity, entity: MessageEntity) {
        MatcherAssert.assertThat(wrapper.id, CoreMatchers.equalTo(entity.id))
    }
}