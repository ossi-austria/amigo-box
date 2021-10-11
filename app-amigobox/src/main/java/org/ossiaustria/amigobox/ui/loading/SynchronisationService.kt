package org.ossiaustria.amigobox.ui.loading

import kotlinx.coroutines.flow.firstOrNull
import org.ossiaustria.lib.domain.repositories.AlbumRepository
import org.ossiaustria.lib.domain.repositories.AlbumShareRepository
import org.ossiaustria.lib.domain.repositories.CallRepository
import org.ossiaustria.lib.domain.repositories.GroupRepository
import org.ossiaustria.lib.domain.repositories.MessageRepository
import org.ossiaustria.lib.domain.repositories.MultimediaRepository
import org.ossiaustria.lib.domain.repositories.NfcTagRepository
import timber.log.Timber
import javax.inject.Inject

class SynchronisationService(
    private val groupRepository: GroupRepository,
    private val albumRepository: AlbumRepository,
    private val albumShareRepository: AlbumShareRepository,
    private val callRepository: CallRepository,
    private val messageRepository: MessageRepository,
    private val multimediaRepository: MultimediaRepository,
    private val nfcTagRepository: NfcTagRepository,
) {

    suspend fun syncGroup() {
        val resource = groupRepository.getAllGroups(refresh = true).firstOrNull { !it.isLoading }
        if (resource == null) {
            Timber.e("Sync Group: FAILED")
        } else {
            val list = resource.valueOrNull()
            Timber.i("Sync Group: ${resource.isSuccess} ${list?.size}")
        }
    }
}