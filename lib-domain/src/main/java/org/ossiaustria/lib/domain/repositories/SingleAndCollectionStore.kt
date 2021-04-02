package org.ossiaustria.lib.domain.repositories

import com.dropbox.android.external.store4.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map
import org.ossiaustria.lib.domain.common.Outcome
import org.ossiaustria.lib.domain.database.AbstractEntityDao
import org.ossiaustria.lib.domain.database.entities.AbstractEntity
import timber.log.Timber
import java.util.*

suspend fun <T> FlowCollector<Outcome<T>>.transformResponseToOutcome(
    response: StoreResponse<T>,
    onNewData: () -> Outcome<T>
) {
    when (response) {
        is StoreResponse.Loading -> {
            Timber.d("[Store 4] Loading from ${response.origin}")
            emit(Outcome.loading())
        }

        is StoreResponse.Error -> {
            Timber.d("[Store 4] Error from ${response.origin}")
            emit(
                Outcome.failure(response.errorMessageOrNull() ?: "Store 4 Error")
            )
        }
        is StoreResponse.Data -> {
            val data = response.value
            Timber.d("[Store 4] Data from ${response.origin}")
            emit(Outcome.success(data))
        }
        is StoreResponse.NoNewData -> {
            Timber.d("[Store 4] NoNewData from ${response.origin}")
            // either use an empty list, fetch from dao, or just signify "still loading" here
            emit(onNewData.invoke())
        }
        else -> {
            throw IllegalStateException("State lost.")
        }
    }
}

abstract class SingleAndCollectionStore<ENTITY : AbstractEntity, WRAPPER, DOMAIN : Any>(
    dao: AbstractEntityDao<ENTITY, WRAPPER>
) {

    protected abstract suspend fun writeItem(item: DOMAIN)
    protected abstract fun readItem(id: UUID): Flow<DOMAIN>
    protected abstract fun readAllItems(): Flow<List<DOMAIN>>
    protected abstract suspend fun fetchOne(id: UUID): DOMAIN
    protected abstract suspend fun fetchAll(): List<DOMAIN>

    @ExperimentalCoroutinesApi
    @FlowPreview
    protected val singleStore: Store<UUID, DOMAIN> = StoreBuilder.from(
        fetcher = Fetcher.of { key: UUID -> fetchOne(key) },
        sourceOfTruth = SourceOfTruth.of(
            reader = { key -> readItem(key) },
            writer = { _: UUID, input: DOMAIN ->
                writeItem(input)
            },
            delete = { key: UUID -> dao.deleteById(key) },
            deleteAll = { dao.deleteAll() }
        )
    ).build()

    @ExperimentalCoroutinesApi
    @FlowPreview
    protected val collectionStore: Store<String, List<DOMAIN>> = StoreBuilder.from(
        fetcher = Fetcher.of { fetchAll() },
        sourceOfTruth = SourceOfTruth.of(
            reader = {
                readAllItems()
            },
            writer = { key: String, input: List<DOMAIN> ->
                Timber.i("Store4 writer $key")
                input.map { writeItem(it) }
            },
            deleteAll = { dao.deleteAll() }
        )
    ).build()

    protected fun withFlowCollection(
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
}