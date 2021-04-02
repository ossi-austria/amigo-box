@file:Suppress("IllegalIdentifier")

package org.ossiaustria.lib.domain.database

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.database.entities.MultimediaEntity
import org.ossiaustria.lib.domain.models.enums.MultimediaType
import org.robolectric.RobolectricTestRunner
import java.util.*


@RunWith(RobolectricTestRunner::class)
internal class MultimediaDaoTest :
    SendableDaoTest<MultimediaEntity, MultimediaEntity, MultimediaDao>() {

    override fun init() {
        dao = db.multimediaDao()
    }

    override fun createEntity(id: UUID): MultimediaEntity {
        return MultimediaEntity(
            id = id,
            createdAt = 1, sendAt = 2, retrievedAt = 3,
            senderId = UUID.randomUUID(), receiverId = UUID.randomUUID(),
            type = MultimediaType.VIDEO,
            albumId = null,
            localUrl = "localurl",
            remoteUrl = "remoteUrl",
            ownerId = UUID.randomUUID()
        )
    }

    override fun permuteEntity(entity: MultimediaEntity): MultimediaEntity {
        return entity.copy(
            createdAt = 101,
            sendAt = 102
        )
    }

    override fun checkEqual(wrapper: MultimediaEntity, entity: MultimediaEntity) {
        MatcherAssert.assertThat(wrapper, CoreMatchers.equalTo(entity))
    }

    override fun checkSameId(wrapper: MultimediaEntity, entity: MultimediaEntity) {
        MatcherAssert.assertThat(wrapper.id, CoreMatchers.equalTo(entity.id))
    }

}