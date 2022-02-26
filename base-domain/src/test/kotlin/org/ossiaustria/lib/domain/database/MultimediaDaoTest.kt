@file:Suppress("IllegalIdentifier")

package org.ossiaustria.lib.domain.database

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.database.entities.MultimediaEntity
import org.ossiaustria.lib.domain.models.enums.MultimediaType
import org.robolectric.RobolectricTestRunner
import java.util.*


@RunWith(RobolectricTestRunner::class)
internal class MultimediaDaoTest :
    DoubleEntityDaoTest<MultimediaEntity, MultimediaEntity, MultimediaDao>() {

    override fun init() {
        dao = db.multimediaDao()
    }

    override fun createEntity(id: UUID): MultimediaEntity {
        return MultimediaEntity(
            id = id,
            createdAt = Date(),
            type = MultimediaType.VIDEO,
            albumId = null,
            contentType = "localurl",
            filename = "remoteUrl",
            ownerId = UUID.randomUUID()
        )
    }

    override fun permuteEntity(entity: MultimediaEntity): MultimediaEntity {
        return entity.copy(
            createdAt = Date(),
        )
    }

    override fun findById(entity: MultimediaEntity): MultimediaEntity {
        return runBlocking { dao.findById(entity.id).take(1).first() }
    }

    override fun deleteById(entity: MultimediaEntity) {
        runBlocking { dao.deleteById(entity.id) }
    }

    override fun checkEqual(wrapper: MultimediaEntity, entity: MultimediaEntity) {
        MatcherAssert.assertThat(wrapper, CoreMatchers.equalTo(entity))
    }

    override fun checkSameId(wrapper: MultimediaEntity, entity: MultimediaEntity) {
        MatcherAssert.assertThat(wrapper.id, CoreMatchers.equalTo(entity.id))
    }

}