package org.ossiaustria.lib.domain.repositories

import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.ossiaustria.lib.commons.DispatcherProvider
import org.ossiaustria.lib.domain.api.CallApi
import org.ossiaustria.lib.domain.common.Outcome
import org.ossiaustria.lib.domain.database.CallDao
import org.ossiaustria.lib.domain.database.entities.CallEntity
import org.ossiaustria.lib.domain.database.entities.toCall
import org.ossiaustria.lib.domain.database.entities.toCallEntity
import org.ossiaustria.lib.domain.models.Call
import timber.log.Timber
import java.util.*


interface CallRepository {

    fun getAllCalls(): Flow<Outcome<List<Call>>>

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    fun getCall(id: UUID): Flow<Outcome<Call>>
}

internal class CallRepositoryImpl(
    private val callApi: CallApi,
    private val callDao: CallDao,
    private val dispatcherProvider: DispatcherProvider
) : CallRepository,
    SingleAndCollectionStore<CallEntity, CallEntity, Call>(callDao) {

    override suspend fun fetchOne(id: UUID): Call = callApi.get(id)
    override suspend fun fetchAll(): List<Call> = callApi.getAll()

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


    override fun readAllItems(): Flow<List<Call>> =
        withFlowCollection(callDao.findAll()) {
            it.toCall()
        }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getAllCalls(): Flow<Outcome<List<Call>>> = flow {
        collectionStore.stream(StoreRequest.cached(key = "all", refresh = true))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<List<Call>> ->
                transformResponseToOutcome(response, onNewData = { Outcome.loading() })
            }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getCall(id: UUID): Flow<Outcome<Call>> = flow {
        singleStore.stream(StoreRequest.cached(key = id, refresh = true))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<Call> ->
                transformResponseToOutcome(response, onNewData = { Outcome.loading() })
            }
    }
}



