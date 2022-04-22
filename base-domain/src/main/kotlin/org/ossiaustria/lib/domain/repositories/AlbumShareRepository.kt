package org.ossiaustria.lib.domain.repositories

import com.dropbox.android.external.store4.StoreResponse
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.ossiaustria.lib.commons.DispatcherProvider
import org.ossiaustria.lib.domain.api.AlbumShareApi
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.database.AlbumShareDao
import org.ossiaustria.lib.domain.database.entities.AlbumShareEntity
import org.ossiaustria.lib.domain.database.entities.AlbumShareEntityWithData
import org.ossiaustria.lib.domain.database.entities.toAlbumShare
import org.ossiaustria.lib.domain.database.entities.toAlbumShareEntity
import org.ossiaustria.lib.domain.models.AlbumShare
import timber.log.Timber
import java.util.*

interface AlbumShareRepository {
    @FlowPreview
    fun getAllAlbumShares(refresh: Boolean = false): Flow<Resource<List<AlbumShare>>>

    @FlowPreview
    fun getAlbumShare(id: UUID, refresh: Boolean = false): Flow<Resource<AlbumShare>>
}

internal class AlbumShareRepositoryImpl(
    private val albumShareApi: AlbumShareApi,
    private val albumShareDao: AlbumShareDao,
    dispatcherProvider: DispatcherProvider
) : AlbumShareRepository,
    SingleAndCollectionStore<AlbumShareEntity, AlbumShareEntityWithData, AlbumShare>(
        albumShareDao,
        dispatcherProvider
    ) {

    override suspend fun fetchOne(id: UUID): AlbumShare = albumShareApi.get(id)
    override suspend fun defaultFetchAll(): List<AlbumShare> = albumShareApi.getAll()

    override suspend fun writeItem(item: AlbumShare) {
        try {
            albumShareDao.insert(item.toAlbumShareEntity())
        } catch (e: Exception) {
            Timber.e(e, "Store4 cannot write item: $item")
        }
    }

    override fun readItem(id: UUID): Flow<AlbumShare> =
        albumShareDao.findById(id).map {
            it.toAlbumShare()
        }

    override fun defaultReadAll(): Flow<List<AlbumShareEntityWithData>> = albumShareDao.findAll()

    @FlowPreview
    override fun getAllAlbumShares(refresh: Boolean): Flow<Resource<List<AlbumShare>>> = flow {
        defaultCollectionStore.stream(newRequest("all", refresh))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<List<AlbumShare>> ->
                transformResponseToOutcome(response, onNoNewData = { Resource.loading() })
            }
    }

    @FlowPreview
    override fun getAlbumShare(id: UUID, refresh: Boolean): Flow<Resource<AlbumShare>> = flow {

        itemTransform(
            singleStore.stream(newRequest(key = id, refresh = refresh))
        )
    }

    override fun transform(item: AlbumShareEntityWithData): AlbumShare = item.toAlbumShare()

}



