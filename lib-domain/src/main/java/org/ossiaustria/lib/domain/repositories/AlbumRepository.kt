package org.ossiaustria.lib.domain.repositories

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.ossiaustria.lib.commons.DispatcherProvider
import org.ossiaustria.lib.domain.api.AlbumApi
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.database.AlbumDao
import org.ossiaustria.lib.domain.database.MultimediaDao
import org.ossiaustria.lib.domain.database.entities.AlbumEntity
import org.ossiaustria.lib.domain.database.entities.AlbumEntityWithData
import org.ossiaustria.lib.domain.database.entities.toAlbum
import org.ossiaustria.lib.domain.database.entities.toAlbumEntity
import org.ossiaustria.lib.domain.database.entities.toMultimediaEntityList
import org.ossiaustria.lib.domain.models.Album
import timber.log.Timber
import java.util.*

interface AlbumRepository {

    fun getAllAlbums(refresh: Boolean = false): Flow<Resource<List<Album>>>

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    fun getAlbum(id: UUID, refresh: Boolean = false): Flow<Resource<Album>>
}

internal class AlbumRepositoryImpl(
    private val albumApi: AlbumApi,
    private val albumDao: AlbumDao,
    private val multimediaDao: MultimediaDao,
    dispatcherProvider: DispatcherProvider
) : AlbumRepository,
    SingleAndCollectionStore<AlbumEntity, AlbumEntityWithData, Album>(
        albumDao,
        dispatcherProvider
    ) {

    override suspend fun fetchOne(id: UUID): Album = albumApi.get(id)
    override suspend fun defaultFetchAll(): List<Album> = albumApi.getShared()

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

    override fun defaultReadAll(): Flow<List<AlbumEntityWithData>> {
        return albumDao.findAll()
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getAllAlbums(refresh: Boolean): Flow<Resource<List<Album>>> = flow {
        listTransform(
            defaultCollectionStore.stream(
                newRequest(key = "all", refresh = refresh)
            )
        )
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getAlbum(id: UUID, refresh: Boolean): Flow<Resource<Album>> = flow {
        itemTransform(
            singleStore.stream(newRequest(key = id, refresh = refresh))
        )
    }

    override fun transform(item: AlbumEntityWithData): Album = item.toAlbum()

}



