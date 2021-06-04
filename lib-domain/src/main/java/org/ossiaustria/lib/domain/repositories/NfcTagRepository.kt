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
import org.ossiaustria.lib.domain.api.NfcTagApi
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.database.NfcTagDao
import org.ossiaustria.lib.domain.database.entities.NfcTagEntity
import org.ossiaustria.lib.domain.database.entities.toNfcTag
import org.ossiaustria.lib.domain.database.entities.toNfcTagEntity
import org.ossiaustria.lib.domain.models.NfcTag
import timber.log.Timber
import java.util.*


interface NfcTagRepository {

    fun getAllNfcTags(): Flow<Resource<List<NfcTag>>>

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    fun getNfcTag(id: UUID): Flow<Resource<NfcTag>>

}

internal class NfcTagRepositoryImpl(
    private val nfcTagApi: NfcTagApi,
    private val nfcTagDao: NfcTagDao,
    dispatcherProvider: DispatcherProvider
) : NfcTagRepository,
    SingleAndCollectionStore<NfcTagEntity, NfcTagEntity, NfcTag>(nfcTagDao, dispatcherProvider) {

    override suspend fun fetchOne(id: UUID): NfcTag = nfcTagApi.get(id)
    override suspend fun defaultFetchAll(): List<NfcTag> = nfcTagApi.getAll()

    override suspend fun writeItem(item: NfcTag) {
        try {
            nfcTagDao.insert(item.toNfcTagEntity())
        } catch (e: Exception) {
            Timber.e(e, "Store4 cannot write item: $item")
        }
    }

    override fun readItem(id: UUID): Flow<NfcTag> =
        withFlowItem(nfcTagDao.findById(id)) {
            it.toNfcTag()
        }


    override fun defaultReadAll(): Flow<List<NfcTagEntity>> = nfcTagDao.findAll()

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getAllNfcTags(): Flow<Resource<List<NfcTag>>> = flow {
        defaultCollectionStore.stream(StoreRequest.cached(key = "all", refresh = true))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<List<NfcTag>> ->
                transformResponseToOutcome(response, onNewData = { Resource.loading() })
            }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getNfcTag(id: UUID): Flow<Resource<NfcTag>> = flow {
        singleStore.stream(StoreRequest.cached(key = id, refresh = true))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<NfcTag> ->
                transformResponseToOutcome(response, onNewData = { Resource.loading() })
            }
    }

    override fun transform(item: NfcTagEntity): NfcTag = item.toNfcTag()

}



