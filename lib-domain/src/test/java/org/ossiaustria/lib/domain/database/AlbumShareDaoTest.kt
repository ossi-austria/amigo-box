package org.ossiaustria.lib.domain.database

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.database.entities.AlbumEntity
import org.ossiaustria.lib.domain.database.entities.AlbumShareEntity
import org.ossiaustria.lib.domain.database.entities.AlbumShareEntityWithData
import org.ossiaustria.lib.domain.database.entities.toAlbumShare
import org.robolectric.RobolectricTestRunner
import java.util.*

@RunWith(RobolectricTestRunner::class)
internal class AlbumShareDaoTest :
    SendableDaoTest<AlbumShareEntity, AlbumShareEntityWithData, AlbumShareDao>() {

    lateinit var albumDao: AlbumDao

    override fun init() {
        dao = db.albumShareDao()
        albumDao = db.albumDao()
    }

    @Test
    fun `mapping should contain album and owner`() {

        runBlocking {
            dao.insert(createEntity())
        }
        val findAll = runBlocking { dao.findAll().take(1).first() }
        val subject = findAll[0].toAlbumShare()
        MatcherAssert.assertThat(subject.album, CoreMatchers.not(CoreMatchers.nullValue()))
    }

    override fun createEntity(id: UUID): AlbumShareEntity {
        val albumId = UUID.randomUUID()
        val personId = UUID.randomUUID()
        val albumEntity = AlbumEntity(
            albumId = albumId,
            ownerId = personId,
            name = "album"
        )
        runBlocking { albumDao.insert(albumEntity) }
        return AlbumShareEntity(
            id = id,
            createdAt = 1,
            sendAt = 2,
            retrievedAt = 3,
            senderId = personId,
            receiverId = personId,
            albumId = albumId,
        )
    }

    override fun permuteEntity(entity: AlbumShareEntity): AlbumShareEntity {
        return entity.copy(
            createdAt = 101,
            sendAt = 102
        )
    }

    override fun checkEqual(wrapper: AlbumShareEntityWithData, entity: AlbumShareEntity) {
        MatcherAssert.assertThat(wrapper.albumShare, CoreMatchers.equalTo(entity))
    }

    override fun checkSameId(wrapper: AlbumShareEntityWithData, entity: AlbumShareEntity) {
        MatcherAssert.assertThat(wrapper.albumShare.id, CoreMatchers.equalTo(entity.id))
    }
}