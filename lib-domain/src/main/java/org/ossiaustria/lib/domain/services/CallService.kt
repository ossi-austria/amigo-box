package org.ossiaustria.lib.domain.services

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.Multimedia
import org.ossiaustria.lib.domain.models.enums.CallType
import org.ossiaustria.lib.domain.models.enums.MultimediaType
import org.ossiaustria.lib.domain.repositories.CallRepository
import org.ossiaustria.lib.domain.services.ServiceMocks.HER_PERSON_ID
import org.ossiaustria.lib.domain.services.ServiceMocks.HIS_ALBUM_ID
import org.ossiaustria.lib.domain.services.ServiceMocks.MY_PERSON_ID
import timber.log.Timber
import java.time.ZonedDateTime
import java.util.*
import java.util.UUID.randomUUID

interface CallService : SendableService<Call> {
    fun informWillJoin()
    fun informTerminated()
    fun informJoined()
    fun informParticipantJoined()
    fun informParticipantLeft()
}

class MockCallServiceImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val callRepository: CallRepository,
) : CallService {

    private val wrapper = SendableServiceWrapper<Call>(ioDispatcher)

     private fun mockCall(
        id: UUID = randomUUID(),
        senderId: UUID = HER_PERSON_ID,
        receiverId: UUID = MY_PERSON_ID,
        callType: CallType = CallType.AUDIO,
        createdAt: Date = Date(),
        sendAt: Date? = Date(),
        retrievedAt: Date? = Date(),
    ) = Call(
        id = id,
        createdAt = createdAt,
        sendAt = sendAt,
        retrievedAt = retrievedAt,
        senderId = senderId,
        receiverId = receiverId,
        callType = callType,
        startedAt = Date(),
        finishedAt = Date(),
    )

    override fun informWillJoin() {
        Timber.i("informWillJoin")
    }

    override fun informTerminated() {
        Timber.i("informTerminated")
    }

    override fun informJoined() {
        Timber.i("informJoined")
    }

    override fun informParticipantJoined() {
        Timber.i("informParticipantJoined")
    }

    override fun informParticipantLeft() {
        Timber.i("informParticipantLeft")
    }

    override fun getOne(id: UUID): Flow<Resource<Call>> = wrapper.getOne {
        mockCall()
    }

    override fun getAll(): Flow<Resource<List<Call>>> = wrapper.getAll {
        (1..200).map {
            val senderId = if (it.even) MY_PERSON_ID else HER_PERSON_ID
            val receiverId = if (it.even) HER_PERSON_ID else MY_PERSON_ID
            mockCall(senderId = senderId, receiverId = receiverId)
        }
    }

    override fun findWithPersons(
        senderId: UUID?,
        receiverId: UUID?
    ): Flow<Resource<List<Call>>> =
        wrapper.getAll {
            (1..4).map {
                mockCall(
                    senderId = senderId ?: MY_PERSON_ID,
                    receiverId = receiverId ?: HER_PERSON_ID
                )
            }
        }

    override fun findWithSender(senderId: UUID): Flow<Resource<List<Call>>> =
        wrapper.getAll {
            (1..4).map { mockCall(senderId = senderId) }
        }

    override fun findWithReceiver(receiverId: UUID): Flow<Resource<List<Call>>> =
        wrapper.getAll {
            (1..4).map { mockCall(receiverId = receiverId) }
        }

    override fun markAsSent(id: UUID, time: ZonedDateTime): Flow<Resource<Call>> =
        wrapper.markAsSent {
            mockCall(id = id, senderId = id, sendAt = Date())
        }

    override fun markAsRetrieved(id: UUID, time: ZonedDateTime): Flow<Resource<Call>> =
        wrapper.markAsRetrieved {
            mockCall(id = id, senderId = id, retrievedAt = Date())
        }

}