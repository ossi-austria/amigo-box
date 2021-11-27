package org.ossiaustria.lib.domain.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.ossiaustria.lib.commons.DispatcherProvider
import org.ossiaustria.lib.domain.api.CallApi
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.database.CallDao
import org.ossiaustria.lib.domain.database.entities.CallEntity
import org.ossiaustria.lib.domain.database.entities.toCall
import org.ossiaustria.lib.domain.database.entities.toCallEntity
import org.ossiaustria.lib.domain.models.Call
import timber.log.Timber
import java.util.*

interface CallRepository {

    fun getAllCalls(refresh: Boolean = false): Flow<Resource<List<Call>>>

    fun getCall(id: UUID, refresh: Boolean = false): Flow<Resource<Call>>
}

internal class CallRepositoryImpl(
    private val callApi: CallApi,
    private val callDao: CallDao,
    dispatcherProvider: DispatcherProvider
) : CallRepository,
    SingleAndCollectionStore<CallEntity, CallEntity, Call>(callDao, dispatcherProvider) {

    override suspend fun fetchOne(id: UUID): Call = callApi.getOne(id)
    override suspend fun defaultFetchAll(): List<Call> = callApi.getOwn()

    override suspend fun writeItem(item: Call) {
        try {
            callDao.insert(item.toCallEntity())
        } catch (e: Exception) {
            Timber.e(e, "Store4 cannot write item: $item")
        }
    }

    override fun readItem(id: UUID): Flow<Call> =
        withFlowItem(callDao.findById(id)) {
            it.toCall()
        }

    override fun defaultReadAll(): Flow<List<CallEntity>> = callDao.findAll()

    override fun getAllCalls(refresh: Boolean): Flow<Resource<List<Call>>> = flow {
        listTransform(
            defaultCollectionStore.stream(newRequest("all", refresh))
        )
    }

    override fun getCall(id: UUID, refresh: Boolean): Flow<Resource<Call>> = flow {
        itemTransform(
            singleStore.stream(newRequest(key = id, refresh = refresh))
        )
    }

    override fun transform(item: CallEntity): Call = item.toCall()

}



