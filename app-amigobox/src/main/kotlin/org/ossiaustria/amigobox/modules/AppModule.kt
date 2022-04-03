package org.ossiaustria.amigobox.modules

import org.koin.dsl.module
import org.ossiaustria.amigobox.AmigoBoxIntentEntryPointProvider
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.cloudmessaging.CloudPushHandlerService
import org.ossiaustria.amigobox.cloudmessaging.FCMHelper
import org.ossiaustria.amigobox.cloudmessaging.IntentEntryPointProvider
import org.ossiaustria.lib.domain.services.AuthService

val appModule = module {

    single<IntentEntryPointProvider> {
        AmigoBoxIntentEntryPointProvider()
    }

    single {
        CloudPushHandlerService(get(), get(), get()).also {
            CloudPushHandlerService.instance = it
        }
    }

    single {
        FCMHelper(get<AuthService>())
    }

    single {
        Navigator()
    }
}
