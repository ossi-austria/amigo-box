package org.ossiaustria.amigobox

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.ossiaustria.amigobox.cloudmessaging.CloudPushHandlerService
import org.ossiaustria.lib.domain.modules.UserContext
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(
        includes = [
            FCMModule::class
        ]
)
object AppModule {

    @Provides
    @Singleton
    fun amigoBoxFCMHandlerService(
            @ApplicationContext appContext: Context,
            userContext: UserContext,
    ): CloudPushHandlerService {
        return CloudPushHandlerService(appContext, userContext).also {
            CloudPushHandlerService.instance = it
        }
    }
}


@InstallIn(SingletonComponent::class)
@Module
object FCMModule {

}