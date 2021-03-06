@file:Suppress("IllegalIdentifier")

package org.ossiaustria.lib.domain.database

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.database.entities.AlbumEntity
import org.ossiaustria.lib.domain.database.entities.AlbumEntityWithData
import org.ossiaustria.lib.domain.database.entities.toAlbum
import org.robolectric.RobolectricTestRunner
import java.util.*

@RunWith(RobolectricTestRunner::class)
internal class AlbumDaoTest : DoubleEntityDaoTest<AlbumEntity, AlbumEntityWithData, AlbumDao>() {

    var personId: UUID = UUID.randomUUID()

    override fun init() {
        dao = db.albumDao()
    }

    @Test
    fun `mapping should contain owner`() {

        runBlocking {
            dao.insert(
                AlbumEntity(
                    albumId = UUID.randomUUID(),
                    ownerId = personId,
                    name = "name"
                )
            )
        }
        // ".take(1).first()" collects the Flow
        val findAll = runBlocking { dao.findAll().take(1).first() }
        val subject = findAll[0].toAlbum()
        assertThat(subject.ownerId, CoreMatchers.not(CoreMatchers.nullValue()))
    }

    override fun createEntity(id: UUID): AlbumEntity {
        return AlbumEntity(
            albumId = UUID.randomUUID(),
            ownerId = personId,
            name = "name"
        )
    }

    override fun permuteEntity(entity: AlbumEntity): AlbumEntity {
        return entity.copy(
            name = "new name",
        )
    }

    override fun findById(entity: AlbumEntity): AlbumEntityWithData {
        return runBlocking { dao.findById(entity.albumId).take(1).first() }
    }

    override fun checkEqual(wrapper: AlbumEntityWithData, entity: AlbumEntity) {
        assertThat(wrapper.album, equalTo(entity))
    }

    override fun checkSameId(wrapper: AlbumEntityWithData, entity: AlbumEntity) {
        assertThat(wrapper.album.albumId, equalTo(entity.albumId))
    }

    override fun deleteById(entity: AlbumEntity) {
        runBlocking { dao.deleteById(entity.albumId) }
    }

}