package org.ossiaustria.lib.domain.repositories

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.ossiaustria.lib.commons.DispatcherProvider
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.database.AbstractEntityDao
import org.ossiaustria.lib.domain.database.entities.AbstractEntity
import timber.log.Timber
import java.util.*

suspend fun <T> FlowCollector<Resource<T>>.transformResponseToOutcome(
    response: StoreResponse<T>,
    onNoNewData: () -> Resource<T>
) {
    when (response) {
        is StoreResponse.Loading -> {
            Timber.d("[Store 4] Loading from ${response.origin}")
            emit(Resource.loading())
        }

        is StoreResponse.Error -> {
            Timber.e("[Store 4] Error from ${response.origin}: ${response.errorMessageOrNull()}")
            try {
                response.throwIfError()
            } catch (e: Exception) {
                Timber.e(e)
            }
            emit(
                Resource.failure(response.errorMessageOrNull() ?: "Store 4 Error")
            )
        }
        is StoreResponse.Data -> {
            val data = response.value
            Timber.i("[Store 4] Data from ${response.origin}")
            emit(Resource.success(data))
        }
        is StoreResponse.NoNewData -> {
            Timber.d("[Store 4] NoNewData from ${response.origin}")
            // either use an empty list, fetch from dao, or just signify "still loading" here
            emit(onNoNewData.invoke())
        }
        else -> {
            throw IllegalStateException("State lost.")
        }
    }
}

abstract class SingleAndCollectionStore<ENTITY : AbstractEntity, WRAPPER, DOMAIN : Any>(
    dao: AbstractEntityDao<ENTITY, WRAPPER>,
    protected val dispatcherProvider: DispatcherProvider
) {

    protected abstract fun transform(item: WRAPPER): DOMAIN
    protected abstract suspend fun writeItem(item: DOMAIN)
    protected abstract fun readItem(id: UUID): Flow<DOMAIN>
    protected abstract fun defaultReadAll(): Flow<List<WRAPPER>>
    protected abstract suspend fun fetchOne(id: UUID): DOMAIN
    protected abstract suspend fun defaultFetchAll(): List<DOMAIN>

    @FlowPreview
    protected val singleStore: Store<UUID, DOMAIN> = StoreBuilder.from(
        fetcher = Fetcher.of { key: UUID -> fetchOne(key) },
        sourceOfTruth = SourceOfTruth.of(
            reader = { key -> readItem(key) },
            writer = { _: UUID, input: DOMAIN -> writeItem(input) },
            delete = { key: UUID -> dao.deleteById(key) },
            deleteAll = { dao.deleteAll() }
        )
    ).build()

    @FlowPreview
    protected val defaultCollectionStore: Store<String, List<DOMAIN>> = buildCollectionStore(
        fetchApi = ::defaultFetchAll,
        readDao = ::defaultReadAll,
        transform = ::transform
    )

    @FlowPreview
    protected fun <KEY : Any> buildCollectionStore(
        fetchApi: suspend () -> List<DOMAIN>,
        readDao: () -> Flow<List<WRAPPER>>,
        transform: (WRAPPER) -> DOMAIN
    ): Store<KEY, List<DOMAIN>> = StoreBuilder.from(
        fetcher = Fetcher.of { fetchApi() },
        sourceOfTruth = SourceOfTruth.of(
            reader = {
                withFlowCollection(readDao()) { transform(it) }
            },
            writer = { key: KEY, input: List<DOMAIN> ->
                Timber.i("Store4 writer $key")
                input.map { writeItem(it) }
            },
            deleteAll = { deleteAll() }
        )
    ).build()

    private fun deleteAll() {

    }

    private fun withFlowCollection(
        itemsFlow: Flow<List<WRAPPER>>,
        transform: (WRAPPER) -> DOMAIN
    ): Flow<List<DOMAIN>> {
        return itemsFlow.map { list ->
            try {
                list.map { transform(it) }
            } catch (e: Exception) {
                Timber.e(e, "Store4 cannot read collection:")
                emptyList<DOMAIN>()
            }
        }
    }

    protected fun withFlowItem(
        itemsFlow: Flow<WRAPPER>,
        transform: (WRAPPER) -> DOMAIN
    ): Flow<DOMAIN> {
        return itemsFlow.map { item ->
            try {
                transform(item)
            } catch (e: Exception) {
                Timber.e(e, "Store4 cannot read item:")
                throw e
            }
        }
    }

    protected fun newRequest(key: String, refresh: Boolean) =
        if (refresh)
            StoreRequest.fresh(key = key)
        else
            StoreRequest.cached(key = key, refresh = true)

    protected fun newRequest(key: UUID, refresh: Boolean) =
        if (refresh)
            StoreRequest.fresh(key = key)
        else
            StoreRequest.cached(key = key, refresh = true)

    protected suspend fun FlowCollector<Resource<DOMAIN>>.itemTransform(
        flow: Flow<StoreResponse<DOMAIN>>
    ) {
        flow.flowOn(dispatcherProvider.io()).collect { response ->
            transformResponseToOutcome(response, onNoNewData = { Resource.loading() })
        }
    }

    protected suspend fun FlowCollector<Resource<List<DOMAIN>>>.listTransform(
        flow: Flow<StoreResponse<List<DOMAIN>>>
    ) {
        flow.flowOn(dispatcherProvider.io()).collect { response ->
            transformResponseToOutcome(response, onNoNewData = { Resource.success(emptyList()) })
        }
    }
}

fun <T> Flow<T>.finished(): Flow<T> {
    return this.filter { it !is Resource.Loading<*> }
}
