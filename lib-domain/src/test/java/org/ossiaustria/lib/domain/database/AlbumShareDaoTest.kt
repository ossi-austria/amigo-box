package org.ossiaustria.lib.domain.database

import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.database.entities.AlbumEntity
import org.ossiaustria.lib.domain.database.entities.AlbumShareEntity
import org.ossiaustria.lib.domain.database.entities.PersonEntity
import org.ossiaustria.lib.domain.database.entities.toAlbumShare
import org.ossiaustria.lib.domain.models.enums.MembershipType
import org.robolectric.RobolectricTestRunner
import java.util.*


@RunWith(RobolectricTestRunner::class)
internal class AlbumShareDaoTest : SendableDaoTest<AlbumShareEntity, AlbumShareDao>() {

    lateinit var albumDao: AlbumDao
    lateinit var personDao: PersonDao

    override fun init() {
        dao = db.albumShareDao()
        albumDao = db.albumDao()
        personDao = db.personDao()
    }

    override fun createEntity(id: UUID): AlbumShareEntity {
        return AlbumShareEntity(
            id = id,
            createdAt = 1, sendAt = 2, retrievedAt = 3,
            senderId = UUID.randomUUID(), receiverId = UUID.randomUUID(),
            albumId = UUID.randomUUID(),
        )
    }

    @Test
    fun `mapping should contain album and owner`() {

        runBlocking {

            val person = PersonEntity(
                personId = UUID.randomUUID(),
                groupId = UUID.randomUUID(),
                email = "email",
                name = "name",
                memberType = MembershipType.MEMBER
            )

            val albumEntity = AlbumEntity(
                albumId = UUID.randomUUID(),
                ownerId = person.personId,
                name = "album"
            )

            personDao.insert(person)
            albumDao.insert(albumEntity)

            dao.insert(
                AlbumShareEntity(
                    id = UUID.randomUUID(),
                    senderId = person.personId,
                    receiverId = person.personId,
                    albumId = albumEntity.albumId
                )
            )
        }
        val findAll = runBlocking { dao.findAllWithData() }
        val subject = findAll[0].toAlbumShare()
        MatcherAssert.assertThat(subject.album, CoreMatchers.not(CoreMatchers.nullValue()))
    }
}