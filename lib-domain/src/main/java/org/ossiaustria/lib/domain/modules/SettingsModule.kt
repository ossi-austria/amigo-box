package org.ossiaustria.lib.domain.modules

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.ossiaustria.lib.domain.repositories.SettingsRepository
import org.ossiaustria.lib.domain.repositories.SettingsRepositoryImpl

val settingsModule = module {

    single<SettingsRepository> {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val aes256Siv = EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV
        val aes256Gcm = EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM

        val encryptedSharedPreferences = EncryptedSharedPreferences.create(
            SettingsRepository.SETTINGS_AMIGO_CRYPTED,
            masterKeyAlias,
            androidContext(),
            aes256Siv,
            aes256Gcm
        )
        SettingsRepositoryImpl(androidContext(), encryptedSharedPreferences)
    }

}