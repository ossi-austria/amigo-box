package org.ossiaustria.lib.domain.services

import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.NfcInfo
import org.ossiaustria.lib.domain.modules.UserContext
import org.ossiaustria.lib.domain.repositories.NfcInfoRepository
import timber.log.Timber
import java.util.*

interface NfcInfoService {
    suspend fun getAll(): Resource<List<NfcInfo>>
    suspend fun findPerRef(ref: String): Resource<NfcInfo>
    suspend fun findPerId(id: UUID): Resource<NfcInfo>
}

class NfcInfoServiceImpl(
    private val nfcInfoRepository: NfcInfoRepository,
    private val userContext: UserContext,
) : NfcInfoService {

    override suspend fun findPerRef(ref: String): Resource<NfcInfo> {
        val resource = nfcInfoRepository
            .getAllNfcTags(true).take(2).last()
        return if (resource.isSuccess) {
            val normalizedRef = normalize(ref)
            val needle =
                resource.valueOrNull()?.find { normalize(it.nfcRef).contains(normalizedRef) }
            if (needle != null) {
                if (needle.ownerId == userContext.personId()) {
                    Resource.success(needle)
                } else {
                    Timber.e("Wrong ownerId in userContext.personId for this NFC !")
                    Resource.success(needle)
                }
            } else {
                Timber.e("No matching ref $ref found in getAllNfcTags")
                Resource.failure("No entity with ref $ref")
            }
        } else {
            Timber.e("No NFC Tags found getAllNfcTags")
            Resource.failure("No entity with ref $ref")
        }
    }

    override suspend fun findPerId(id: UUID): Resource<NfcInfo> =
        nfcInfoRepository.getNfcTag(id, true).debounce(1000L).last()

    override suspend fun getAll(): Resource<List<NfcInfo>> =
        nfcInfoRepository.getAllNfcTags(false).debounce(1000L).last()

    companion object {
        fun normalize(ref: String) = ref.replace(":", "")
    }
}
