package org.ossiaustria.amigobox.ui.loading

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.repositories.AlbumRepository
import org.ossiaustria.lib.domain.repositories.AlbumShareRepository
import org.ossiaustria.lib.domain.repositories.CallRepository
import org.ossiaustria.lib.domain.repositories.GroupRepository
import org.ossiaustria.lib.domain.repositories.MessageRepository
import org.ossiaustria.lib.domain.repositories.MultimediaRepository
import org.ossiaustria.lib.domain.repositories.NfcInfoRepository
import timber.log.Timber

class SynchronisationService(
    private val groupRepository: GroupRepository,
    private val albumRepository: AlbumRepository,
    private val albumShareRepository: AlbumShareRepository,
    private val callRepository: CallRepository,
    private val messageRepository: MessageRepository,
    private val multimediaRepository: MultimediaRepository,
    private val nfcInfoRepository: NfcInfoRepository,
) {

    suspend fun syncEverything() {
        syncGroups()
        syncAlbums()
//        syncAlbumShares()
        syncCalls()
        syncMessages()
        syncMultimedias()
        syncNfcTags()
    }

    suspend fun syncGroups() = syncCollection("syncGroups") {
        groupRepository.getAllGroups(refresh = true)
    }

    suspend fun syncAlbums() = syncCollection("syncAlbums") {
        albumRepository.getAllAlbums(refresh = true)
    }

    suspend fun syncAlbumShares() = syncCollection("syncAlbumShares") {
        albumShareRepository.getAllAlbumShares(refresh = true)
    }

    suspend fun syncCalls() = syncCollection("syncCalls") {
        callRepository.getAllCalls(refresh = true)
    }

    suspend fun syncMessages() = syncCollection("syncMessages") {
        messageRepository.getAllMessages(refresh = true)
    }

    suspend fun syncMultimedias() = syncCollection("syncMultimedias") {
        multimediaRepository.getAllMultimedias(refresh = true)
    }

    suspend fun syncNfcTags() = syncCollection("syncNfcTags") {
        nfcInfoRepository.getAllNfcTags(refresh = true)
    }

    private suspend fun <T> syncCollection(
        methodName: String,
        lambda: () -> Flow<Resource<List<T>>>
    ): Resource<List<T>>? {
        val resource = lambda.invoke().firstOrNull { !it.isLoading }
        if (resource == null) {
            Timber.e("$methodName: FAILED")
        } else {
            val list = resource.valueOrNull()
            if (resource.isFailure) {
                Timber.e(resource.throwableOrNull(),methodName)
            } else {
                Timber.i("$methodName: ${resource.isSuccess} ${list?.size}")
            }
        }
        return resource
    }
}