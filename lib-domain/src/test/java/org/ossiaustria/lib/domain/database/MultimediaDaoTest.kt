@file:Suppress("IllegalIdentifier")

package org.ossiaustria.lib.domain.database

import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.database.entities.MultimediaEntity
import org.ossiaustria.lib.domain.models.enums.MultimediaType
import org.robolectric.RobolectricTestRunner
import java.util.*


@RunWith(RobolectricTestRunner::class)
internal class MultimediaDaoTest : SendableDaoTest<MultimediaEntity, MultimediaDao>() {

    override fun init() {
        dao = db.multimediaDao()
    }

    override fun createEntity(id: UUID): MultimediaEntity {
        return MultimediaEntity(
            id = id,
            createdAt = 1, sendAt = 2, retrievedAt = 3,
            senderId = UUID.randomUUID(), receiverId = UUID.randomUUID(),
            type = MultimediaType.VIDEO,
            collectionId = null,
            localUrl = "localurl",
            remoteUrl = "remoteUrl",
            ownerId = UUID.randomUUID()
        )
    }
}