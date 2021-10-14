package org.ossiaustria.amigobox.modules

import org.koin.dsl.module
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.cloudmessaging.CloudPushHandlerService
import org.ossiaustria.amigobox.cloudmessaging.FCMHelper
import org.ossiaustria.lib.domain.services.AuthService

val appModule = module {
    single {
        CloudPushHandlerService(get()).also {
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
