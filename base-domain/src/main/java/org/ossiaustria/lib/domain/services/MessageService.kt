package org.ossiaustria.lib.domain.services

import kotlinx.coroutines.flow.Flow
import org.ossiaustria.lib.domain.api.MessageApi
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Message
import org.ossiaustria.lib.domain.repositories.MessageRepository
import java.util.*

interface MessageService : SendableService<Message> {
    suspend fun createMessage(senderId: UUID, receiverId: UUID, text: String): Resource<Message>
    suspend fun markAsRetrieved(id: UUID): Resource<Message>
}

class MessageServiceImpl(
    private val messageRepository: MessageRepository,
    private val messageApi: MessageApi
) : MessageService {

    override suspend fun createMessage(
        senderId: UUID,
        receiverId: UUID,
        text: String
    ): Resource<Message> =
        try {
            Resource.success(messageApi.createMessage(receiverId = receiverId, text = text))
        } catch (e: Exception) {
            Resource.failure(e)
        }

    override suspend fun markAsRetrieved(id: UUID): Resource<Message> =
        try {
            Resource.success(messageApi.markAsRetrieved(id))
        } catch (e: Exception) {
            Resource.failure(e)
        }

    override fun getOne(id: UUID): Flow<Resource<Message>> =
        messageRepository.getMessage(id)

    override fun getAll(): Flow<Resource<List<Message>>> =
        messageRepository.getAllMessages(false)

    override fun findWithPersons(
        senderId: UUID?,
        receiverId: UUID?
    ): Flow<Resource<List<Message>>> =
        messageRepository.getAllMessages(true)

    override fun findWithSender(senderId: UUID): Flow<Resource<List<Message>>> =
        messageRepository.getAllMessages(false)

    override fun findWithReceiver(receiverId: UUID): Flow<Resource<List<Message>>> =
        messageRepository.getAllMessages(false)

}