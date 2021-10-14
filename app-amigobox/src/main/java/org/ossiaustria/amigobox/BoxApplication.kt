package org.ossiaustria.amigobox

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.ossiaustria.amigobox.cloudmessaging.CloudPushHandlerService
import org.ossiaustria.amigobox.cloudmessaging.FCMHelper
import org.ossiaustria.amigobox.cloudmessaging.NotificationChannels
import org.ossiaustria.amigobox.modules.appModule
import org.ossiaustria.amigobox.modules.appServiceModule
import org.ossiaustria.amigobox.modules.viewModelsModule
import org.ossiaustria.lib.domain.auth.AuthInterceptor
import org.ossiaustria.lib.domain.modules.UserContext
import org.ossiaustria.lib.domain.modules.apiModule
import org.ossiaustria.lib.domain.modules.databaseModule
import org.ossiaustria.lib.domain.modules.dispatcherModule
import org.ossiaustria.lib.domain.modules.repositoryModule
import org.ossiaustria.lib.domain.modules.serviceModule
import org.ossiaustria.lib.domain.modules.settingsModule
import org.ossiaustria.lib.domain.repositories.SettingsRepository
import timber.log.Timber

class BoxApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@BoxApplication)
            modules(
                settingsModule,
                dispatcherModule,
                apiModule,
                databaseModule,
                repositoryModule,
                serviceModule,
                appServiceModule,
                viewModelsModule,
                appModule,
            )
        }

        val settingsRepository: SettingsRepository by inject()
        val authInterceptor: AuthInterceptor by inject()
        val userContext: UserContext by inject()
        val fcmHelper: FCMHelper by inject()
        val cloudPushHandlerService: CloudPushHandlerService by inject()

        authInterceptor.initToken(settingsRepository.accessToken)
        userContext.initContext(
            settingsRepository.accessToken,
            settingsRepository.account,
            settingsRepository.currentPerson
        )
        NotificationChannels.createAllChannels(this)
        // Assert FCM token
        fcmHelper.getToken { Timber.i("Retrieved token: $it") }
        CloudPushHandlerService.instance = cloudPushHandlerService
    }
}