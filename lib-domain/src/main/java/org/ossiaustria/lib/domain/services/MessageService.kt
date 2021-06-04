package org.ossiaustria.lib.domain.services

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Message
import org.ossiaustria.lib.domain.repositories.MessageRepository
import org.ossiaustria.lib.domain.services.ServiceMocks.HER_PERSON_ID
import org.ossiaustria.lib.domain.services.ServiceMocks.MY_PERSON_ID
import timber.log.Timber
import java.time.ZonedDateTime
import java.util.*
import java.util.UUID.randomUUID

val Int.even: Boolean
    get() = this % 2 == 0


interface MessageService : SendableService<Message> {
    fun createMessage(senderId: UUID, receiverId: UUID, text: String): Flow<Resource<Message>>
}

class MockMessageServiceImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val messageRepository: MessageRepository,
) : MessageService {

    private val wrapper = SendableServiceWrapper<Message>(ioDispatcher)

    private fun mockMessage(
        id: UUID = randomUUID(),
        senderId: UUID = HER_PERSON_ID,
        receiverId: UUID = MY_PERSON_ID,
        text: String = "mock message",
        createdAt: Long = System.currentTimeMillis(),
        sendAt: Long? = System.currentTimeMillis(),
        retrievedAt: Long? = System.currentTimeMillis(),
    ) = Message(
        id = id,
        createdAt = createdAt,
        sendAt = sendAt,
        retrievedAt = retrievedAt,
        senderId = senderId,
        receiverId = receiverId,
        text = text
    )

    override fun createMessage(
        senderId: UUID,
        receiverId: UUID,
        text: String
    ): Flow<Resource<Message>> = flow {
        emit(Resource.loading())
        emit(
            Resource.success(
                mockMessage(senderId = MY_PERSON_ID, receiverId = HER_PERSON_ID, retrievedAt = null)
            )
        )
    }.catch {
        Timber.e(it)
        emit(Resource.failure(it))
    }.flowOn(ioDispatcher)

    override fun getOne(id: UUID): Flow<Resource<Message>> = wrapper.getOne {
        mockMessage()
    }

    override fun getAll(): Flow<Resource<List<Message>>> = wrapper.getAll {
        runBlocking {
            messageRepository.getAllMessages().toList().flatMap { it.valueOrNull()!! }
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
    ): Flow<Resource<List<Message>>> =
        wrapper.getAll {
            (1..4).map {
                mockMessage(
                    senderId = senderId ?: MY_PERSON_ID,
                    receiverId = receiverId ?: HER_PERSON_ID
                )
            }
        }

    override fun findWithSender(senderId: UUID): Flow<Resource<List<Message>>> =
        wrapper.getAll {
            (1..4).map { mockMessage(senderId = senderId) }
        }

    override fun findWithReceiver(receiverId: UUID): Flow<Resource<List<Message>>> =
        wrapper.getAll {
            (1..4).map { mockMessage(receiverId = receiverId) }
        }

    override fun markAsSent(id: UUID, time: ZonedDateTime): Flow<Resource<Message>> =
        wrapper.markAsSent {
            mockMessage(id = id, senderId = id, sendAt = System.currentTimeMillis())
        }

    override fun markAsRetrieved(id: UUID, time: ZonedDateTime): Flow<Resource<Message>> =
        wrapper.markAsRetrieved {
            mockMessage(id = id, senderId = id, retrievedAt = System.currentTimeMillis())
        }

}