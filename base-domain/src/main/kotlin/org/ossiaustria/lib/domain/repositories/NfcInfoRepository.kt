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
import org.ossiaustria.lib.domain.models.AmigoNfcInfo
import timber.log.Timber
import java.util.*

interface NfcInfoRepository {

    @FlowPreview
    fun getAllNfcTags(refresh: Boolean = false): Flow<Resource<List<AmigoNfcInfo>>>

    @FlowPreview
    fun getNfcTag(id: UUID, refresh: Boolean = false): Flow<Resource<AmigoNfcInfo>>
}

internal class NfcInfoRepositoryImpl(
    private val nfcInfoApi: NfcInfoApi,
    private val nfcInfoDao: NfcInfoDao,
    dispatcherProvider: DispatcherProvider
) : NfcInfoRepository,
    SingleAndCollectionStore<NfcInfoEntity, NfcInfoEntity, AmigoNfcInfo>(
        nfcInfoDao,
        dispatcherProvider
    ) {

    override suspend fun fetchOne(id: UUID): AmigoNfcInfo = nfcInfoApi.getOne(id)
    override suspend fun defaultFetchAll(): List<AmigoNfcInfo> = nfcInfoApi.getAllAccessibleNfcs()

    override suspend fun writeItem(item: AmigoNfcInfo) {
        try {
            nfcInfoDao.insert(item.toNfcTagEntity())
        } catch (e: Exception) {
            Timber.e(e, "Store4 cannot write item: $item")
        }
    }

    override fun readItem(id: UUID): Flow<AmigoNfcInfo> =
        withFlowItem(nfcInfoDao.findById(id)) {
            it.toNfcTag()
        }

    override fun defaultReadAll(): Flow<List<NfcInfoEntity>> = nfcInfoDao.findAll()

    @FlowPreview
    override fun getAllNfcTags(refresh: Boolean): Flow<Resource<List<AmigoNfcInfo>>> = flow {
        listTransform(
            defaultCollectionStore.stream(newRequest(key = "all", refresh = refresh))
        )
    }

    @FlowPreview
    override fun getNfcTag(id: UUID, refresh: Boolean): Flow<Resource<AmigoNfcInfo>> = flow {
        itemTransform(
            singleStore.stream(newRequest(key = id, refresh = refresh))
        )
    }

    override fun transform(item: NfcInfoEntity): AmigoNfcInfo = item.toNfcTag()

}



