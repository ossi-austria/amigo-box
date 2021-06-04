package org.ossiaustria.lib.domain.services

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.Multimedia
import org.ossiaustria.lib.domain.models.enums.MultimediaType
import org.ossiaustria.lib.domain.repositories.MultimediaRepository
import org.ossiaustria.lib.domain.services.ServiceMocks.HER_PERSON_ID
import org.ossiaustria.lib.domain.services.ServiceMocks.HIS_ALBUM_ID
import org.ossiaustria.lib.domain.services.ServiceMocks.MY_PERSON_ID
import java.time.ZonedDateTime
import java.util.*
import java.util.UUID.randomUUID


interface MultimediaService : SendableService<Multimedia> {
}

class MockMultimediaServiceImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val multimediaRepository: MultimediaRepository,
) : MultimediaService {

    private val wrapper = SendableServiceWrapper<Multimedia>(ioDispatcher)

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
        senderId: UUID = HER_PERSON_ID,
        receiverId: UUID = MY_PERSON_ID,
        ownerId: UUID = HER_PERSON_ID,
        createdAt: Long = System.currentTimeMillis(),
        sendAt: Long? = System.currentTimeMillis(),
        retrievedAt: Long? = System.currentTimeMillis(),
    ) = Multimedia(
        id = id,
        createdAt = createdAt,
        sendAt = sendAt,
        retrievedAt = retrievedAt,
        senderId = senderId,
        receiverId = receiverId,
        ownerId = ownerId,
        remoteUrl = "https://en.wikipedia.org/wiki/Image#/media/File:Image_created_with_a_mobile_phone.png",
        localUrl = "",
        type = MultimediaType.IMAGE
    )


    override fun getOne(id: UUID): Flow<Resource<Multimedia>> = wrapper.getOne {
        mockMultimedia()
    }

    override fun getAll(): Flow<Resource<List<Multimedia>>> = wrapper.getAll {
        (1..200).map {
            val senderId = if (it.even) MY_PERSON_ID else HER_PERSON_ID
            val receiverId = if (it.even) HER_PERSON_ID else MY_PERSON_ID
            mockMultimedia(senderId = senderId, receiverId = receiverId)
        }
    }

    override fun findWithPersons(
        senderId: UUID?,
        receiverId: UUID?
    ): Flow<Resource<List<Multimedia>>> =
        wrapper.getAll {
            (1..4).map {
                mockMultimedia(
                    senderId = senderId ?: MY_PERSON_ID,
                    receiverId = receiverId ?: HER_PERSON_ID
                )
            }
        }

    override fun findWithSender(senderId: UUID): Flow<Resource<List<Multimedia>>> =
        wrapper.getAll {
            (1..4).map { mockMultimedia(senderId = senderId) }
        }

    override fun findWithReceiver(receiverId: UUID): Flow<Resource<List<Multimedia>>> =
        wrapper.getAll {
            (1..4).map { mockMultimedia(receiverId = receiverId) }
        }

    override fun markAsSent(id: UUID, time: ZonedDateTime): Flow<Resource<Multimedia>> =
        wrapper.markAsSent {
            mockMultimedia(id = id, senderId = id, sendAt = System.currentTimeMillis())
        }

    override fun markAsRetrieved(id: UUID, time: ZonedDateTime): Flow<Resource<Multimedia>> =
        wrapper.markAsRetrieved {
            mockMultimedia(id = id, senderId = id, retrievedAt = System.currentTimeMillis())
        }

}