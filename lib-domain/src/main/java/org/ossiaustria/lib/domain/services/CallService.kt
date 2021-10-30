package org.ossiaustria.lib.domain.services

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.ossiaustria.lib.domain.api.CallApi
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.CallType
import org.ossiaustria.lib.domain.repositories.CallRepository
import java.util.*

interface CallService : SendableService<Call> {
    fun accept(call: Call): Flow<Resource<Call>>
    fun deny(call: Call): Flow<Resource<Call>>
    fun cancel(call: Call): Flow<Resource<Call>>
    fun finish(call: Call): Flow<Resource<Call>>
    fun createCall(person: Person, callType: CallType): Flow<Resource<Call>>
}

class CallServiceImpl(
    ioDispatcher: CoroutineDispatcher,
    private val callRepository: CallRepository,
    private val callApi: CallApi,
) : CallService {

    private val wrapper = SendableServiceWrapper<Call>(ioDispatcher)

    override fun createCall(person: Person, callType: CallType): Flow<Resource<Call>> =
        wrapper.getOne {
            callApi.createCall(callType.toString(), person.id)
        }

    override fun accept(call: Call): Flow<Resource<Call>> = wrapper.getOne {
        callApi.acceptCall(call.id)
    }

    override fun deny(call: Call): Flow<Resource<Call>> = wrapper.getOne {
        callApi.denyCall(call.id)
    }

    override fun cancel(call: Call): Flow<Resource<Call>> = wrapper.getOne {
        callApi.cancelCall(call.id)
    }

    override fun finish(call: Call): Flow<Resource<Call>> = wrapper.getOne {
        callApi.finishCall(call.id)
    }

    override fun getOne(id: UUID): Flow<Resource<Call>> =
        callRepository.getCall(id, true)

    override fun getAll(): Flow<Resource<List<Call>>> =
        callRepository.getAllCalls(true)

    override fun findWithPersons(
        senderId: UUID?,
        receiverId: UUID?
    ): Flow<Resource<List<Call>>> =
        callRepository.getAllCalls(true)

    override fun findWithSender(senderId: UUID): Flow<Resource<List<Call>>> =
        callRepository.getAllCalls()

    override fun findWithReceiver(receiverId: UUID): Flow<Resource<List<Call>>> =
        callRepository.getAllCalls()

}