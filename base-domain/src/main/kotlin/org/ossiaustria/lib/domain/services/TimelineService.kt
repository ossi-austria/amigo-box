package org.ossiaustria.lib.domain.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Sendable
import org.ossiaustria.lib.domain.repositories.finished
import java.util.*

interface TimelineService {
    fun findWithPersons(senderId: UUID?, receiverId: UUID?): Flow<Resource<List<Sendable>>>
    fun findWithSender(senderId: UUID): Flow<Resource<List<Sendable>>>
    suspend fun findWithReceiver(receiverId: UUID): List<Sendable>
}

class TimelineServiceImpl(
    val callService: CallService,
    val messageService: MessageService,
) : TimelineService {

    override fun findWithPersons(
        senderId: UUID?,
        receiverId: UUID?
    ): Flow<Resource<List<Sendable>>> {
        val calls = callService.findWithPersons(senderId, receiverId).finished()
        val messages = messageService.findWithPersons(senderId, receiverId).finished()
        return merge(calls, messages)
    }

    override fun findWithSender(senderId: UUID): Flow<Resource<List<Sendable>>> {
        val calls = callService.findWithSender(senderId).finished()
        val messages = messageService.findWithSender(senderId).finished()
        return merge(calls, messages)
    }

    override suspend fun findWithReceiver(receiverId: UUID): List<Sendable> {
        val messages =
            messageService.findWithReceiver(receiverId)
                .finished()
                .mapNotNull { it.valueOrNull() }.first()
        val calls =
            callService.findWithReceiver(receiverId)
                .finished()
                .mapNotNull { it.valueOrNull() }.first()
        return listOf(messages, calls).flatten()
    }

}