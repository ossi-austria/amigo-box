package org.ossiaustria.lib.domain.repositories

import com.dropbox.android.external.store4.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.ossiaustria.lib.commons.DispatcherProvider
import org.ossiaustria.lib.domain.api.AlbumApi
import org.ossiaustria.lib.domain.common.Outcome
import org.ossiaustria.lib.domain.database.AlbumDao
import org.ossiaustria.lib.domain.database.MultimediaDao
import org.ossiaustria.lib.domain.database.PersonDao
import org.ossiaustria.lib.domain.database.entities.*
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
    private val personDao: PersonDao,
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
                val item = input.toAlbumEntity()
                val items = input.toMultimediaEntityList()
                albumDao.insert(item)
                multimediaDao.insertAll(items)
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
            reader = { key ->
                runBlocking {
                    try {
                        Timber.i("Store4 reader $key")
                        albumDao.findAll().take(1).first()
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
                val map = albumDao.findAll().map {
                    try {
                        it.toAlbumList()
                    } catch (e: Exception) {
                        Timber.e(e)
                        emptyList<Album>()
                    }
                }
                map
            },
            writer = { key: String, input: List<Album> ->
                Timber.i("Store4 writer $key")
                input.map {
                    val item = it.toAlbumEntity()
                    val items = it.toMultimediaEntityList()

                    try {
                        albumDao.insert(item)
                        personDao.insert(it.owner.toPersonEntity())
                        multimediaDao.insertAll(items)
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            },
            deleteAll = { albumDao.deleteAll() }
        )
    ).build()

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



