package org.ossiaustria.lib.domain.repositories

import com.dropbox.android.external.store4.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.ossiaustria.lib.commons.DispatcherProvider
import org.ossiaustria.lib.domain.api.AuthorApi
import org.ossiaustria.lib.domain.common.Outcome
import org.ossiaustria.lib.domain.database.AuthorDao
import org.ossiaustria.lib.domain.models.Author


//https://medium.com/swlh/introduction-to-android-data-storage-with-dropbox-store-4-b2dc7e3753e1
interface AuthorRepository {

    suspend fun getAllAuthors(): Flow<Outcome<List<Author>>>

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    suspend fun getAuthor(id: Long): Flow<Outcome<Author>>
}

class AuthorRepositoryImpl(
    private val api: AuthorApi,
    private val dao: AuthorDao,
    private val dispatcher: DispatcherProvider
) : AuthorRepository, AbstractRepository() {

    @ExperimentalCoroutinesApi
    @FlowPreview
    private val singleStore: Store<Long, Author> = StoreBuilder.from(
        fetcher = Fetcher.of { key: Long -> api.get(key) },
        sourceOfTruth = SourceOfTruth.of(
            reader = { key -> dao.find(key) },
            writer = { _: Long, input: Author -> dao.update(input) },
            delete = { key: Long -> dao.delete(key) },
            deleteAll = { dao.deleteAll() }
        )
    ).build()

    @ExperimentalCoroutinesApi
    @FlowPreview
    private val collectionStore: Store<String, List<Author>> = StoreBuilder.from(
        fetcher = Fetcher.of { api.getAll() },
        sourceOfTruth = SourceOfTruth.of(
            reader = { dao.findAll() },
            writer = { _: String, input: List<Author> -> dao.insertAll(input) },
            deleteAll = { dao.deleteAll() }
        )
    ).build()

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override suspend fun getAllAuthors(): Flow<Outcome<List<Author>>> = flow {
        collectionStore.stream(StoreRequest.cached(key = "all", refresh = true))
            .flowOn(dispatcher.io())
            .collect { response: StoreResponse<List<Author>> ->
                transformResponseToOutcome(response, onNewData = { Outcome.loading() })
            }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override suspend fun getAuthor(id: Long): Flow<Outcome<Author>> = flow {
        singleStore.stream(StoreRequest.cached(key = id, refresh = true))
            .flowOn(dispatcher.io())
            .collect { response: StoreResponse<Author> ->
                transformResponseToOutcome(response, onNewData = { Outcome.loading() })
            }
    }
}



