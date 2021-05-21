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
import org.ossiaustria.lib.domain.api.MultimediaApi
import org.ossiaustria.lib.domain.common.Effect
import org.ossiaustria.lib.domain.database.MultimediaDao
import org.ossiaustria.lib.domain.database.entities.MultimediaEntity
import org.ossiaustria.lib.domain.database.entities.toMultimedia
import org.ossiaustria.lib.domain.database.entities.toMultimediaEntity
import org.ossiaustria.lib.domain.models.Multimedia
import timber.log.Timber
import java.util.*


interface MultimediaRepository {

    fun getAllMultimedias(): Flow<Effect<List<Multimedia>>>

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    fun getMultimedia(id: UUID): Flow<Effect<Multimedia>>
}

internal class MultimediaRepositoryImpl(
    private val multimediaApi: MultimediaApi,
    private val multimediaDao: MultimediaDao,
    private val dispatcherProvider: DispatcherProvider
) : MultimediaRepository,
    SingleAndCollectionStore<MultimediaEntity, MultimediaEntity, Multimedia>(multimediaDao) {

    override suspend fun fetchOne(id: UUID): Multimedia = multimediaApi.get(id)
    override suspend fun fetchAll(): List<Multimedia> = multimediaApi.getAll()

    override suspend fun writeItem(item: Multimedia) {
        try {
            multimediaDao.insert(item.toMultimediaEntity())
        } catch (e: Exception) {
            Timber.e(e, "Store4 cannot write item: $item")
        }
    }

    override fun readItem(id: UUID): Flow<Multimedia> =
        withFlowItem(multimediaDao.findById(id)) {
            it.toMultimedia()
        }


    override fun readAllItems(): Flow<List<Multimedia>> =
        withFlowCollection(multimediaDao.findAll()) {
            it.toMultimedia()
        }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getAllMultimedias(): Flow<Effect<List<Multimedia>>> = flow {
        collectionStore.stream(StoreRequest.cached(key = "all", refresh = true))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<List<Multimedia>> ->
                transformResponseToOutcome(response, onNewData = { Effect.loading() })
            }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getMultimedia(id: UUID): Flow<Effect<Multimedia>> = flow {
        singleStore.stream(StoreRequest.cached(key = id, refresh = true))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<Multimedia> ->
                transformResponseToOutcome(response, onNewData = { Effect.loading() })
            }
    }
}



