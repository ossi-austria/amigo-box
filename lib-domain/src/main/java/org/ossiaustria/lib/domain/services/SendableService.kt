package org.ossiaustria.lib.domain.services

import kotlinx.coroutines.flow.Flow
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Sendable
import java.time.ZonedDateTime
import java.util.*


interface SendableService<S : Sendable> {
    fun getOne(id: UUID): Flow<Resource<S>>
    fun getAll(): Flow<Resource<List<S>>>
    fun findWithPersons(senderId: UUID?, receiverId: UUID?): Flow<Resource<List<S>>>
    fun findWithSender(senderId: UUID): Flow<Resource<List<S>>>
    fun findWithReceiver(receiverId: UUID): Flow<Resource<List<S>>>

    // operations for marking
    fun markAsSent(id: UUID, time: ZonedDateTime): Flow<Resource<S>>
    fun markAsRetrieved(id: UUID, time: ZonedDateTime): Flow<Resource<S>>
}
