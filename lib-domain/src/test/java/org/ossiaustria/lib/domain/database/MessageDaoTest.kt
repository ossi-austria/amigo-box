@file:Suppress("IllegalIdentifier")

package org.ossiaustria.lib.domain.database

import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.database.entities.MessageEntity
import org.robolectric.RobolectricTestRunner
import java.util.*


@RunWith(RobolectricTestRunner::class)
internal class MessageDaoTest : SendableDaoTest<MessageEntity, MessageDao>() {

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
}