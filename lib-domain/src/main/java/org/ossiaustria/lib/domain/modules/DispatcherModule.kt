package org.ossiaustria.lib.domain.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import org.ossiaustria.lib.commons.DefaultDispatcherProvider
import org.ossiaustria.lib.commons.DispatcherProvider
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DispatcherModule {

    @Provides
    @Singleton
    fun dispatcherProvider(): DispatcherProvider {
        return DefaultDispatcherProvider()
    }

    @Provides
    @Singleton
    fun defaultDispatcher(): CoroutineDispatcher {
        return DefaultDispatcherProvider().io()
    }
}