package org.ossiaustria.lib.domain.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.NfcInfo
import org.ossiaustria.lib.domain.modules.UserContext
import org.ossiaustria.lib.domain.repositories.NfcInfoRepository
import timber.log.Timber
import java.util.*

interface NfcInfoService {
    fun getAll(): Flow<Resource<List<NfcInfo>>>
    fun findPerRef(ref: String): Flow<Resource<NfcInfo>>
    fun findPerId(id: UUID): Flow<Resource<NfcInfo>>
}

class NfcInfoServiceImpl(
    private val nfcInfoRepository: NfcInfoRepository,
    private val userContext: UserContext
) : NfcInfoService {

    override fun findPerRef(ref: String): Flow<Resource<NfcInfo>> =
        nfcInfoRepository.getAllNfcTags(true).map { result ->
            if (result.isSuccess) {
                val needle = result.valueOrNull()?.find { it.nfcRef.contains(ref) }
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

    override fun findPerId(id: UUID): Flow<Resource<NfcInfo>> = nfcInfoRepository.getNfcTag(id, true)
    override fun getAll(): Flow<Resource<List<NfcInfo>>> = nfcInfoRepository.getAllNfcTags(false)
}
