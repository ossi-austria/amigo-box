package org.ossiaustria.lib.domain.services

import kotlinx.coroutines.flow.Flow
import org.ossiaustria.lib.domain.api.CallApi
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.CallType
import org.ossiaustria.lib.domain.repositories.CallRepository
import java.util.*

interface CallService : SendableService<Call> {
    suspend fun accept(call: Call): Resource<Call>
    suspend fun deny(call: Call): Resource<Call>
    suspend fun cancel(call: Call): Resource<Call>
    suspend fun finish(call: Call): Resource<Call>
    suspend fun createCall(person: Person, callType: CallType): Resource<Call>
}

class CallServiceImpl(
    private val callRepository: CallRepository,
    private val callApi: CallApi,
) : CallService {

    override suspend fun createCall(person: Person, callType: CallType): Resource<Call> =
        try {
            Resource.success(callApi.createCall(callType.toString(), person.id))
        } catch (e: Exception) {
            Resource.failure(e)
        }

    override suspend fun accept(call: Call): Resource<Call> = try {
        Resource.success(callApi.acceptCall(call.id))
    } catch (e: Exception) {
        Resource.failure(e)
    }

    override suspend fun deny(call: Call): Resource<Call> = try {
        Resource.success(callApi.denyCall(call.id))
    } catch (e: Exception) {
        Resource.failure(e)
    }

    override suspend fun cancel(call: Call): Resource<Call> = try {
        Resource.success(callApi.cancelCall(call.id))
    } catch (e: Exception) {
        Resource.failure(e)
    }

    override suspend fun finish(call: Call): Resource<Call> = try {
        Resource.success(callApi.finishCall(call.id))
    } catch (e: Exception) {
        Resource.failure(e)
    }

    override fun getOne(id: UUID): Flow<Resource<Call>> =
        callRepository.getCall(id, true)

    override fun getAll(): Flow<Resource<List<Call>>> =
        callRepository.getAllCalls(false)

    override fun findWithPersons(
        senderId: UUID?,
        receiverId: UUID?
    ): Flow<Resource<List<Call>>> =
        callRepository.getAllCalls(false)

    override fun findWithSender(senderId: UUID): Flow<Resource<List<Call>>> =
        callRepository.getAllCalls()

    override fun findWithReceiver(receiverId: UUID): Flow<Resource<List<Call>>> =
        callRepository.getAllCalls()

}