package org.ossiaustria.lib.domain.repositories

import com.dropbox.android.external.store4.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.ossiaustria.lib.commons.DispatcherProvider
import org.ossiaustria.lib.domain.api.AlbumApi
import org.ossiaustria.lib.domain.common.Outcome
import org.ossiaustria.lib.domain.database.AlbumDao
import org.ossiaustria.lib.domain.database.MultimediaDao
import org.ossiaustria.lib.domain.database.entities.toAlbum
import org.ossiaustria.lib.domain.database.entities.toAlbumEntity
import org.ossiaustria.lib.domain.database.entities.toAlbumList
import org.ossiaustria.lib.domain.database.entities.toMultimediaEntityList
import org.ossiaustria.lib.domain.models.Album
import java.util.*


interface AlbumRepository {

    suspend fun getAllAlbums(): Flow<Outcome<List<Album>>>

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    suspend fun getAlbum(id: UUID): Flow<Outcome<Album>>

}

internal class AlbumRepositoryImpl(
    private val albumApi: AlbumApi,
    private val albumDao: AlbumDao,
    private val multimediaDao: MultimediaDao,
    private val dispatcherProvider: DispatcherProvider
) : AlbumRepository, AbstractRepository() {

    @ExperimentalCoroutinesApi
    @FlowPreview
    private val singleStore: Store<UUID, Album> = StoreBuilder.from(
        fetcher = Fetcher.of { key: UUID -> albumApi.get(key) },
        sourceOfTruth = SourceOfTruth.of(
            reader = { key -> albumDao.findById(key).map { it.toAlbum() } },
            writer = { _: UUID, input: Album ->
                albumDao.insert(input.toAlbumEntity())
                multimediaDao.insertAll(input.toMultimediaEntityList())
            },
            delete = { key: UUID -> albumDao.deleteById(key) },
            deleteAll = { albumDao.deleteAll() }
        )
    ).build()

    @ExperimentalCoroutinesApi
    @FlowPreview
    private val collectionStore: Store<String, List<Album>> = StoreBuilder.from(
        fetcher = Fetcher.of { albumApi.getAll() },
        sourceOfTruth = SourceOfTruth.of(
            reader = { albumDao.findAll().map { it.toAlbumList() } },
            writer = { _: String, input: List<Album> -> albumDao.insertAll(input.map(Album::toAlbumEntity)) },
            deleteAll = { albumDao.deleteAll() }
        )
    ).build()

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override suspend fun getAllAlbums(): Flow<Outcome<List<Album>>> = flow {
        collectionStore.stream(StoreRequest.cached(key = "all", refresh = true))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<List<Album>> ->
                transformResponseToOutcome(response, onNewData = { Outcome.loading() })
            }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override suspend fun getAlbum(id: UUID): Flow<Outcome<Album>> = flow {
        singleStore.stream(StoreRequest.cached(key = id, refresh = true))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<Album> ->
                transformResponseToOutcome(response, onNewData = { Outcome.loading() })
            }
    }
}



