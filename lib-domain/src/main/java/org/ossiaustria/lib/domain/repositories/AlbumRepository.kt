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
import kotlinx.coroutines.flow.map
import org.ossiaustria.lib.commons.DispatcherProvider
import org.ossiaustria.lib.domain.api.AlbumApi
import org.ossiaustria.lib.domain.common.Effect
import org.ossiaustria.lib.domain.database.AlbumDao
import org.ossiaustria.lib.domain.database.MultimediaDao
import org.ossiaustria.lib.domain.database.entities.AlbumEntity
import org.ossiaustria.lib.domain.database.entities.AlbumEntityWithData
import org.ossiaustria.lib.domain.database.entities.toAlbum
import org.ossiaustria.lib.domain.database.entities.toAlbumEntity
import org.ossiaustria.lib.domain.database.entities.toAlbumList
import org.ossiaustria.lib.domain.database.entities.toMultimediaEntityList
import org.ossiaustria.lib.domain.models.Album
import timber.log.Timber
import java.util.*


interface AlbumRepository {

    fun getAllAlbums(): Flow<Effect<List<Album>>>

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    fun getAlbum(id: UUID): Flow<Effect<Album>>

}

internal class AlbumRepositoryImpl(
    private val albumApi: AlbumApi,
    private val albumDao: AlbumDao,
    private val multimediaDao: MultimediaDao,
    private val dispatcherProvider: DispatcherProvider
) : AlbumRepository,
    SingleAndCollectionStore<AlbumEntity, AlbumEntityWithData, Album>(albumDao) {


    override suspend fun fetchOne(id: UUID): Album = albumApi.get(id)
    override suspend fun fetchAll(): List<Album> = albumApi.getAll()

    override suspend fun writeItem(item: Album) {
        try {
            albumDao.insert(item.toAlbumEntity())
            multimediaDao.insertAll(item.toMultimediaEntityList())
        } catch (e: Exception) {
            Timber.e(e, "Store4 cannot write item")
        }
    }

    override fun readItem(id: UUID): Flow<Album> {
        return albumDao.findById(id).map { it.toAlbum() }
    }

    override fun readAllItems(): Flow<List<Album>> {
        return albumDao.findAll().map {
            try {
                it.toAlbumList()
            } catch (e: Exception) {
                Timber.e(e, "Store4 cannot read collection")
                emptyList<Album>()
            }
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getAllAlbums(): Flow<Effect<List<Album>>> = flow {
        collectionStore.stream(StoreRequest.cached(key = "all", refresh = true))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<List<Album>> ->
                transformResponseToOutcome(response, onNewData = { Effect.loading() })
            }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getAlbum(id: UUID): Flow<Effect<Album>> = flow {
        singleStore.stream(StoreRequest.cached(key = id, refresh = true))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<Album> ->
                transformResponseToOutcome(response, onNewData = { Effect.loading() })
            }
    }


}



