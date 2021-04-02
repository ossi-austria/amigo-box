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
import org.ossiaustria.lib.domain.api.AlbumShareApi
import org.ossiaustria.lib.domain.common.Outcome
import org.ossiaustria.lib.domain.database.AlbumShareDao
import org.ossiaustria.lib.domain.database.entities.AlbumShareEntity
import org.ossiaustria.lib.domain.database.entities.AlbumShareEntityWithData
import org.ossiaustria.lib.domain.database.entities.toAlbumShare
import org.ossiaustria.lib.domain.database.entities.toAlbumShareEntity
import org.ossiaustria.lib.domain.models.AlbumShare
import timber.log.Timber
import java.util.*


interface AlbumShareRepository {

    fun getAllAlbumShares(): Flow<Outcome<List<AlbumShare>>>

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    fun getAlbumShare(id: UUID): Flow<Outcome<AlbumShare>>
}

internal class AlbumShareRepositoryImpl(
    private val albumShareApi: AlbumShareApi,
    private val albumShareDao: AlbumShareDao,
    private val dispatcherProvider: DispatcherProvider
) : AlbumShareRepository,
    SingleAndCollectionStore<AlbumShareEntity, AlbumShareEntityWithData, AlbumShare>(albumShareDao) {

    override suspend fun fetchOne(id: UUID): AlbumShare = albumShareApi.get(id)
    override suspend fun fetchAll(): List<AlbumShare> = albumShareApi.getAll()

    override suspend fun writeItem(item: AlbumShare) {
        try {
            albumShareDao.insert(item.toAlbumShareEntity())
        } catch (e: Exception) {
            Timber.e(e, "Store4 cannot write item: $item")
        }
    }

    override fun readItem(id: UUID): Flow<AlbumShare> =
        withFlowItem(albumShareDao.findById(id)) {
            it.toAlbumShare()
        }

    override fun readAllItems(): Flow<List<AlbumShare>> =
        withFlowCollection(albumShareDao.findAll()) {
            it.toAlbumShare()
        }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getAllAlbumShares(): Flow<Outcome<List<AlbumShare>>> = flow {
        collectionStore.stream(StoreRequest.cached(key = "all", refresh = true))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<List<AlbumShare>> ->
                transformResponseToOutcome(response, onNewData = { Outcome.loading() })
            }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getAlbumShare(id: UUID): Flow<Outcome<AlbumShare>> = flow {
        singleStore.stream(StoreRequest.cached(key = id, refresh = true))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<AlbumShare> ->
                transformResponseToOutcome(response, onNewData = { Outcome.loading() })
            }
    }
}



