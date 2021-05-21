package org.ossiaustria.lib.domain.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.ossiaustria.lib.domain.repositories.SettingsRepository
import org.ossiaustria.lib.domain.repositories.SettingsRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SettingsModule {

    @Provides
    @Singleton
    fun sharedPreferences(@ApplicationContext appContext: Context): SettingsRepository {
        return SettingsRepositoryImpl(appContext)
    }
}