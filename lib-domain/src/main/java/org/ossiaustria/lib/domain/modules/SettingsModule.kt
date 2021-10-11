package org.ossiaustria.lib.domain.modules

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.ossiaustria.lib.domain.repositories.SettingsRepository
import org.ossiaustria.lib.domain.repositories.SettingsRepositoryImpl

val settingsModule = module {
    single<SharedPreferences> {
        EncryptedSharedPreferences.create(
            SettingsRepository.SETTINGS_AMIGO_CRYPTED,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            androidContext(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
    single<SettingsRepository> {
        SettingsRepositoryImpl(androidContext(), get())
    }

}