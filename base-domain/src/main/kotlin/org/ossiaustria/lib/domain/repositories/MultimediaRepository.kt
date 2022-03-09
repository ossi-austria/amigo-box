package org.ossiaustria.lib.domain.repositories

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.ossiaustria.lib.commons.DispatcherProvider
import org.ossiaustria.lib.domain.api.MultimediaApi
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.database.MultimediaDao
import org.ossiaustria.lib.domain.database.entities.MultimediaEntity
import org.ossiaustria.lib.domain.database.entities.toMultimedia
import org.ossiaustria.lib.domain.database.entities.toMultimediaEntity
import org.ossiaustria.lib.domain.models.Multimedia
import timber.log.Timber
import java.util.*

interface MultimediaRepository {

    fun getAllMultimedias(refresh: Boolean = false): Flow<Resource<List<Multimedia>>>

        fun getMultimedia(id: UUID, refresh: Boolean = false): Flow<Resource<Multimedia>>
}

internal class MultimediaRepositoryImpl(
    private val multimediaApi: MultimediaApi,
    private val multimediaDao: MultimediaDao,
    dispatcherProvider: DispatcherProvider
) : MultimediaRepository,
    SingleAndCollectionStore<MultimediaEntity, MultimediaEntity, Multimedia>(
        multimediaDao,
        dispatcherProvider
    ) {

    override suspend fun fetchOne(id: UUID): Multimedia = multimediaApi.getOne(id)
    override suspend fun defaultFetchAll(): List<Multimedia> = multimediaApi.getShared()

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

    override fun defaultReadAll(): Flow<List<MultimediaEntity>> = multimediaDao.findAll()

    @FlowPreview
        override fun getAllMultimedias(refresh: Boolean): Flow<Resource<List<Multimedia>>> = flow {
        listTransform(
            defaultCollectionStore.stream(newRequest(key = "all", refresh = refresh))
        )
    }

    @FlowPreview
        override fun getMultimedia(id: UUID, refresh: Boolean): Flow<Resource<Multimedia>> = flow {
        itemTransform(
            singleStore.stream(newRequest(key = id, refresh = refresh))
        )
    }

    override fun transform(item: MultimediaEntity): Multimedia = item.toMultimedia()

}



