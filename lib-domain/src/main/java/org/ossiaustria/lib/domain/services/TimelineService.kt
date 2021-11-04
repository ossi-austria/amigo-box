package org.ossiaustria.lib.domain.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Sendable
import java.util.*

interface TimelineService {
    fun findWithPersons(senderId: UUID?, receiverId: UUID?): Flow<Resource<List<Sendable>>>
    fun findWithSender(senderId: UUID): Flow<Resource<List<Sendable>>>
    fun findWithReceiver(receiverId: UUID): Flow<Resource<List<Sendable>>>
}

class TimelineServiceImpl(
    val albumShareService: AlbumShareService,
    val callService: CallService,
    val messageService: MessageService,
    val multimediaService: MultimediaService,
) : TimelineService {

        override fun findWithPersons(
        senderId: UUID?,
        receiverId: UUID?
    ): Flow<Resource<List<Sendable>>> {
        val albumShares = albumShareService.findWithPersons(senderId, receiverId)
        val calls = callService.findWithPersons(senderId, receiverId)
        val messages = messageService.findWithPersons(senderId, receiverId)
        return merge(albumShares, calls, messages)
    }

        override fun findWithSender(senderId: UUID): Flow<Resource<List<Sendable>>> {
        val albumShares = albumShareService.findWithSender(senderId)
        val calls = callService.findWithSender(senderId)
        val messages = messageService.findWithSender(senderId)
        return merge(albumShares, calls, messages)
    }

        override fun findWithReceiver(receiverId: UUID): Flow<Resource<List<Sendable>>> {
        val albumShares = albumShareService.findWithReceiver(receiverId)
        val calls = callService.findWithReceiver(receiverId)
        val messages = messageService.findWithReceiver(receiverId)
        return merge(albumShares, calls, messages)
    }

}