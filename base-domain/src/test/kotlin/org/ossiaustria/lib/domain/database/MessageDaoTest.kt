@file:Suppress("IllegalIdentifier")

package org.ossiaustria.lib.domain.database

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.database.entities.MessageEntity
import org.robolectric.RobolectricTestRunner
import java.util.*
import java.util.UUID.randomUUID

@RunWith(RobolectricTestRunner::class)
internal class MessageDaoTest : SendableDaoTest<MessageEntity, MessageEntity, MessageDao>() {

    override fun init() {
        dao = db.messageDao()
    }

    override fun createEntity(id: UUID): MessageEntity {
        return MessageEntity(
            id = id,
            createdAt = Date(),
            sendAt = Date(),
            retrievedAt = Date(),
            senderId = randomUUID(),
            receiverId = randomUUID(),
            text = randomUUID().toString()
        )
    }

    override fun permuteEntity(entity: MessageEntity): MessageEntity {
        return entity.copy(
            createdAt = Date(),
            sendAt = Date()
        )
    }

    override fun checkEqual(wrapper: MessageEntity, entity: MessageEntity) {
        MatcherAssert.assertThat(wrapper, CoreMatchers.equalTo(entity))
    }

    override fun checkSameId(wrapper: MessageEntity, entity: MessageEntity) {
        MatcherAssert.assertThat(wrapper.id, CoreMatchers.equalTo(entity.id))
    }
}