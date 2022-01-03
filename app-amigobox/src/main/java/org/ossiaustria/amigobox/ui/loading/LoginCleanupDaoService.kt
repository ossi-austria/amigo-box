package org.ossiaustria.amigobox.ui.loading

import org.ossiaustria.lib.domain.database.AlbumDao
import org.ossiaustria.lib.domain.database.AlbumShareDao
import org.ossiaustria.lib.domain.database.CallDao
import org.ossiaustria.lib.domain.database.GroupDao
import org.ossiaustria.lib.domain.database.MessageDao
import org.ossiaustria.lib.domain.database.MultimediaDao
import org.ossiaustria.lib.domain.database.NfcInfoDao
import org.ossiaustria.lib.domain.database.PersonDao
import org.ossiaustria.lib.domain.services.LoginCleanupService
import timber.log.Timber

class LoginCleanupDaoService(
    private val albumDao: AlbumDao,
    private val albumShareDao: AlbumShareDao,
    private val callDao: CallDao,
    private val groupDao: GroupDao,
    private val messageDao: MessageDao,
    private val multimediaDao: MultimediaDao,
    private val nfcInfoDao: NfcInfoDao,
    private val personDao: PersonDao,
) : LoginCleanupService {

    override suspend fun cleanup() {
        Timber.w("cleanup! Delete all locally stored data")
        albumDao.deleteAll()
        albumShareDao.deleteAll()
        callDao.deleteAll()
        groupDao.deleteAll()
        messageDao.deleteAll()
        multimediaDao.deleteAll()
        nfcInfoDao.deleteAll()
        personDao.deleteAll()
    }
}