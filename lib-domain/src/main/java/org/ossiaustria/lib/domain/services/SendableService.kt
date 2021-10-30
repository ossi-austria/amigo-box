package org.ossiaustria.lib.domain.services

import kotlinx.coroutines.flow.Flow
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Sendable
import java.util.*

interface SendableService<S : Sendable> {
    fun getOne(id: UUID): Flow<Resource<S>>
    fun getAll(): Flow<Resource<List<S>>>
    fun findWithPersons(senderId: UUID?, receiverId: UUID?): Flow<Resource<List<S>>>
    fun findWithSender(senderId: UUID): Flow<Resource<List<S>>>
    fun findWithReceiver(receiverId: UUID): Flow<Resource<List<S>>>
}
