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
import timber.log.Timber
import java.util.*


interface AlbumRepository {

    fun getAllAlbums(): Flow<Outcome<List<Album>>>

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    fun getAlbum(id: UUID): Flow<Outcome<Album>>

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
                writeItem(input)
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
            reader = {
                albumDao.findAll().map {
                    try {
                        it.toAlbumList()
                    } catch (e: Exception) {
                        Timber.e(e, "Store4 cannot read collection")
                        emptyList<Album>()
                    }
                }
            },
            writer = { key: String, input: List<Album> ->
                Timber.i("Store4 writer $key")
                input.map { writeItem(it) }
            },
            deleteAll = { albumDao.deleteAll() }
        )
    ).build()

    private suspend fun writeItem(it: Album) {
        try {
            albumDao.insert(it.toAlbumEntity())
            multimediaDao.insertAll(it.toMultimediaEntityList())
        } catch (e: Exception) {
            Timber.e(e, "Store4 cannot write item")
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getAllAlbums(): Flow<Outcome<List<Album>>> = flow {
        collectionStore.stream(StoreRequest.cached(key = "all", refresh = true))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<List<Album>> ->
                transformResponseToOutcome(response, onNewData = { Outcome.loading() })
            }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getAlbum(id: UUID): Flow<Outcome<Album>> = flow {
        singleStore.stream(StoreRequest.cached(key = id, refresh = true))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<Album> ->
                transformResponseToOutcome(response, onNewData = { Outcome.loading() })
            }
    }
}



