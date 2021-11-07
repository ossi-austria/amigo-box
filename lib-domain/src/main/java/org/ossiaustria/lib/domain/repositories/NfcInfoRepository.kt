package org.ossiaustria.lib.domain.repositories

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.ossiaustria.lib.commons.DispatcherProvider
import org.ossiaustria.lib.domain.api.NfcInfoApi
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.database.NfcInfoDao
import org.ossiaustria.lib.domain.database.entities.NfcInfoEntity
import org.ossiaustria.lib.domain.database.entities.toNfcTag
import org.ossiaustria.lib.domain.database.entities.toNfcTagEntity
import org.ossiaustria.lib.domain.models.NfcInfo
import timber.log.Timber
import java.util.*

interface NfcInfoRepository {

    @FlowPreview
    fun getAllNfcTags(refresh: Boolean = false): Flow<Resource<List<NfcInfo>>>

    @FlowPreview
    fun getNfcTag(id: UUID, refresh: Boolean = false): Flow<Resource<NfcInfo>>
}

internal class NfcInfoRepositoryImpl(
    private val nfcInfoApi: NfcInfoApi,
    private val nfcInfoDao: NfcInfoDao,
    dispatcherProvider: DispatcherProvider
) : NfcInfoRepository,
    SingleAndCollectionStore<NfcInfoEntity, NfcInfoEntity, NfcInfo>(
        nfcInfoDao,
        dispatcherProvider
    ) {

    override suspend fun fetchOne(id: UUID): NfcInfo = nfcInfoApi.getOne(id)
    override suspend fun defaultFetchAll(): List<NfcInfo> = nfcInfoApi.getAllAccessibleNfcs()

    override suspend fun writeItem(item: NfcInfo) {
        try {
            nfcInfoDao.insert(item.toNfcTagEntity())
        } catch (e: Exception) {
            Timber.e(e, "Store4 cannot write item: $item")
        }
    }

    override fun readItem(id: UUID): Flow<NfcInfo> =
        withFlowItem(nfcInfoDao.findById(id)) {
            it.toNfcTag()
        }

    override fun defaultReadAll(): Flow<List<NfcInfoEntity>> = nfcInfoDao.findAll()

    @FlowPreview
    override fun getAllNfcTags(refresh: Boolean): Flow<Resource<List<NfcInfo>>> = flow {
        listTransform(
            defaultCollectionStore.stream(newRequest(key = "all", refresh = refresh))
        )
    }

    @FlowPreview
    override fun getNfcTag(id: UUID, refresh: Boolean): Flow<Resource<NfcInfo>> = flow {
        itemTransform(
            singleStore.stream(newRequest(key = id, refresh = refresh))
        )
    }

    override fun transform(item: NfcInfoEntity): NfcInfo = item.toNfcTag()

}



