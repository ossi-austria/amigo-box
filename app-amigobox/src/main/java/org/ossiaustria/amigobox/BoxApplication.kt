package org.ossiaustria.amigobox

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.ossiaustria.amigobox.cloudmessaging.CloudPushHandlerService
import org.ossiaustria.amigobox.cloudmessaging.FCMHelper
import org.ossiaustria.amigobox.cloudmessaging.NotificationChannels
import org.ossiaustria.lib.domain.auth.AuthInterceptor
import org.ossiaustria.lib.domain.modules.UserContext
import org.ossiaustria.lib.domain.repositories.SettingsRepository
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BoxApplication : Application() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var authInterceptor: AuthInterceptor

    @Inject
    lateinit var userContext: UserContext

    @Inject
    lateinit var cloudPushHandlerService: CloudPushHandlerService

    override fun onCreate() {
        super.onCreate()

        authInterceptor.initToken(settingsRepository.accessToken)
        userContext.initContext(settingsRepository.account)

        Timber.plant(Timber.DebugTree())

        NotificationChannels.createAllChannels(this)
        // Assert FCM token
        FCMHelper.getToken { Timber.i("Retrieved token: $it") }
        CloudPushHandlerService.instance = cloudPushHandlerService
    }
}