package org.ossiaustria.lib.domain.services

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import org.ossiaustria.lib.domain.models.Sendable
import org.ossiaustria.lib.domain.repositories.finished
import java.util.*

interface TimelineService {
    suspend fun findWithReceiver(receiverId: UUID): List<Sendable>
}

class TimelineServiceImpl(
    private val callService: CallService,
    private val messageService: MessageService,
) : TimelineService {

    override suspend fun findWithReceiver(receiverId: UUID): List<Sendable> {
        val messages =
            messageService.getAll()
                .finished()
                .mapNotNull { it.valueOrNull() }.first().filter { it.receiverId == receiverId }
        val calls =
            callService.getAll()
                .finished()
                .mapNotNull { it.valueOrNull() }.first()
                .filter { it.isDone() && (it.receiverId == receiverId || it.senderId == receiverId) }
        return listOf(messages, calls).flatten()
    }

}