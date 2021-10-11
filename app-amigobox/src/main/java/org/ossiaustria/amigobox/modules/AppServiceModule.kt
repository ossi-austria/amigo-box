package org.ossiaustria.amigobox.modules

import org.koin.dsl.module
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.cloudmessaging.CloudPushHandlerService
import org.ossiaustria.amigobox.cloudmessaging.FCMHelper
import org.ossiaustria.amigobox.ui.loading.SynchronisationService
import org.ossiaustria.lib.domain.services.AuthService

val appServiceModule = module {
    single<SynchronisationService> {
        SynchronisationService(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }
}
