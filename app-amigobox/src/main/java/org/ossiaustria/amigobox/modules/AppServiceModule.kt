package org.ossiaustria.amigobox.modules

import org.koin.dsl.module
import org.ossiaustria.amigobox.ui.loading.LoginCleanupDaoService
import org.ossiaustria.amigobox.ui.loading.SynchronisationService
import org.ossiaustria.lib.domain.services.LoginCleanupService

val appServiceModule = module {
    single<SynchronisationService> {
        SynchronisationService(
            get(), get(), get(), get(),
            get(), get(), get(),
        )
    }

    single<LoginCleanupService> {
        LoginCleanupDaoService(
            get(), get(), get(), get(),
            get(), get(), get(), get(),
        )
    }
}
