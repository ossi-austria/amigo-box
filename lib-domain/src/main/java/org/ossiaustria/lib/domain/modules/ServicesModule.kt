package org.ossiaustria.lib.domain.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import org.ossiaustria.lib.domain.auth.AuthApi
import org.ossiaustria.lib.domain.repositories.SettingsRepository
import org.ossiaustria.lib.domain.services.AuthService
import org.ossiaustria.lib.domain.services.AuthServiceImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ServicesModule {

    @Provides
    @Singleton
    fun settingsRepository(
        ioDispatcher: CoroutineDispatcher,
        authApi: AuthApi,
        settingsRepository: SettingsRepository
    ): AuthService {
        return AuthServiceImpl(ioDispatcher, authApi, settingsRepository)
    }
}