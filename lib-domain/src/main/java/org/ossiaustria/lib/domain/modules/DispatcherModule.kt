package org.ossiaustria.lib.domain.modules

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.dsl.module
import org.ossiaustria.lib.commons.DefaultDispatcherProvider
import org.ossiaustria.lib.commons.DispatcherProvider

val dispatcherModule = module {
    single<DispatcherProvider> {
        DefaultDispatcherProvider()
    }
    single<CoroutineDispatcher> {
        get<DispatcherProvider>().io()
    }
}

