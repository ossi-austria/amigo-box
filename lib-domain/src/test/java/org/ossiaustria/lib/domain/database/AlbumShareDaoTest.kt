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
import java.util.UUID.randomUUID

@RunWith(RobolectricTestRunner::class)
internal class AlbumShareDaoTest :
    SendableDaoTest<AlbumShareEntity, AlbumShareEntityWithData, AlbumShareDao>() {

    lateinit var albumDao: AlbumDao

    companion object {
        var counter = 0
    }
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
        runBlocking {
            dao.deleteAll()
        }
    }

    override fun createEntity(id: UUID): AlbumShareEntity {
        val albumId = randomUUID()
        val personId = randomUUID()
        val albumEntity = AlbumEntity(
            albumId = albumId,
            ownerId = personId,
            name = "album"
        )
        runBlocking { albumDao.insert(albumEntity) }
        return AlbumShareEntity(
            id = id,
            createdAt = Date(),
            sendAt = Date(),
            retrievedAt = Date(),
            senderId = personId,
            receiverId = personId,
            albumId = albumId,
        )
    }

    override fun permuteEntity(entity: AlbumShareEntity): AlbumShareEntity {
        return entity.copy(
            createdAt = Date(),
            sendAt = Date(),
            retrievedAt = Date(System.currentTimeMillis()+counter)
        )
    }

    override fun checkEqual(wrapper: AlbumShareEntityWithData, entity: AlbumShareEntity) {
        MatcherAssert.assertThat(wrapper.albumShare, CoreMatchers.equalTo(entity))
    }

    override fun checkSameId(wrapper: AlbumShareEntityWithData, entity: AlbumShareEntity) {
        MatcherAssert.assertThat(wrapper.albumShare.id, CoreMatchers.equalTo(entity.id))
    }
}