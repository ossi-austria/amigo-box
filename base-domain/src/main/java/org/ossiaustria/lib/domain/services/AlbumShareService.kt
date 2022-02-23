package org.ossiaustria.lib.domain.services

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.AlbumShare
import org.ossiaustria.lib.domain.models.Multimedia
import org.ossiaustria.lib.domain.models.enums.MultimediaType
import org.ossiaustria.lib.domain.repositories.AlbumShareRepository
import org.ossiaustria.lib.domain.services.ServiceMocks.HER_PERSON_ID
import org.ossiaustria.lib.domain.services.ServiceMocks.HIS_ALBUM_ID
import org.ossiaustria.lib.domain.services.ServiceMocks.MY_PERSON_ID
import java.util.*
import java.util.UUID.randomUUID

interface AlbumShareService : SendableService<AlbumShare> {
    fun markAsSent(id: UUID): Flow<Resource<AlbumShare>>
    fun markAsRetrieved(id: UUID): Flow<Resource<AlbumShare>>
}

@Deprecated("Replace with implementation")
class MockAlbumShareServiceImpl(
    ioDispatcher: CoroutineDispatcher,
    private val albumShareRepository: AlbumShareRepository,
) : AlbumShareService {

    private val wrapper = SendableServiceWrapper<AlbumShare>(ioDispatcher)

    private val album = Album(
        id = HIS_ALBUM_ID,
        name = "Album",
        ownerId = MY_PERSON_ID,
        items = listOf(
            mockMultimedia(ownerId = MY_PERSON_ID),
            mockMultimedia(ownerId = MY_PERSON_ID),
        )
    )

    private fun mockMultimedia(
        id: UUID = randomUUID(),
        ownerId: UUID = HER_PERSON_ID,
        createdAt: Date = Date(),
    ) = Multimedia(
        id = id,
        createdAt = createdAt,
        ownerId = ownerId,
        filename = "https://en.wikipedia.org/wiki/Image#/media/File:Image_created_with_a_mobile_phone.png",
        contentType = "",
        type = MultimediaType.IMAGE
    )

    private fun mockAlbumShare(
        id: UUID = randomUUID(),
        senderId: UUID = HER_PERSON_ID,
        receiverId: UUID = MY_PERSON_ID,
        text: String = "mock message",
        createdAt: Date = Date(),
        sendAt: Date? = Date(),
        retrievedAt: Date? = Date(),
    ) = AlbumShare(
        id = id,
        createdAt = createdAt,
        sentAt = sendAt,
        retrievedAt = retrievedAt,
        senderId = senderId,
        receiverId = receiverId,
        album = album
    )

    override fun getOne(id: UUID): Flow<Resource<AlbumShare>> = wrapper.getOne {
        mockAlbumShare()
    }

    override fun getAll(): Flow<Resource<List<AlbumShare>>> = wrapper.getAll {
        runBlocking {
            albumShareRepository.getAllAlbumShares().toList().flatMap { it.valueOrNull()!! }
        }
//        (1..200).map {
//            val senderId = if (it.even) MY_PERSON_ID else HER_PERSON_ID
//            val receiverId = if (it.even) HER_PERSON_ID else MY_PERSON_ID
//            mockMessage(text = "message $it", senderId = senderId, receiverId = receiverId)
//        }
    }

    override fun findWithPersons(
        senderId: UUID?,
        receiverId: UUID?
    ): Flow<Resource<List<AlbumShare>>> =
        wrapper.getAll {
            (1..4).map {
                mockAlbumShare(
                    senderId = senderId ?: MY_PERSON_ID,
                    receiverId = receiverId ?: HER_PERSON_ID
                )
            }
        }

    override fun findWithSender(senderId: UUID): Flow<Resource<List<AlbumShare>>> =
        wrapper.getAll {
            (1..4).map { mockAlbumShare(senderId = senderId) }
        }

    override fun findWithReceiver(receiverId: UUID): Flow<Resource<List<AlbumShare>>> =
        wrapper.getAll {
            (1..4).map { mockAlbumShare(receiverId = receiverId) }
        }

    override fun markAsSent(id: UUID): Flow<Resource<AlbumShare>> =
        wrapper.markAsSent {
            mockAlbumShare(id = id, senderId = id, sendAt = Date())
        }

    override fun markAsRetrieved(id: UUID): Flow<Resource<AlbumShare>> =
        wrapper.markAsRetrieved {
            mockAlbumShare(id = id, senderId = id, retrievedAt = Date())
        }

}