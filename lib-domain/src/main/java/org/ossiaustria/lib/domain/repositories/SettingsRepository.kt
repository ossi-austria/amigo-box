package org.ossiaustria.lib.domain.repositories

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import org.ossiaustria.lib.domain.auth.Account
import org.ossiaustria.lib.domain.auth.SetFcmTokenRequest
import org.ossiaustria.lib.domain.auth.TokenResult
import org.ossiaustria.lib.domain.repositories.SettingsRepository.Companion.KEY_ACCESS_TOKEN
import org.ossiaustria.lib.domain.repositories.SettingsRepository.Companion.KEY_ACCOUNT
import org.ossiaustria.lib.domain.repositories.SettingsRepository.Companion.KEY_FCM_TOKEN
import org.ossiaustria.lib.domain.repositories.SettingsRepository.Companion.KEY_REFRESH_TOKEN
import timber.log.Timber

interface SettingsRepository {
    companion object {
        const val SETTINGS_AMIGO = "SETTINGS_AMIGO"
        const val SETTINGS_AMIGO_CRYPTED = "SETTINGS_AMIGO_CRYPTED"
        const val KEY_ACCOUNT = "KEY_ACCOUNT"
        const val KEY_REFRESH_TOKEN = "KEY_REFRESH_TOKEN"
        const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"
        const val KEY_FCM_TOKEN = "KEY_FCM_TOKEN"
    }

    var account: Account?
    var fcmToken: String?
    var refreshToken: TokenResult?
    var accessToken: TokenResult?
}

class SettingsRepositoryImpl(
    appContext: Context,
    val cryptedPreferences : SharedPreferences
) : SettingsRepository {

    @VisibleForTesting
    val sharedPreferences: SharedPreferences = appContext.getSharedPreferences(
        SettingsRepository.SETTINGS_AMIGO,
        Context.MODE_PRIVATE
    )


    override var account: Account?
        set(value) = putSecureJson(KEY_ACCOUNT, value)
        get() = getSecureJson(KEY_ACCOUNT, Account::class.java)

    override var refreshToken: TokenResult?
        set(value) = putSecureJson(KEY_REFRESH_TOKEN, value)
        get() = getSecureJson(KEY_REFRESH_TOKEN, TokenResult::class.java)


    override var accessToken: TokenResult?
        set(value) = putSecureJson(KEY_ACCESS_TOKEN, value)
        get() = getSecureJson(KEY_ACCESS_TOKEN, TokenResult::class.java)

    override var fcmToken: String?
        set(value) = putSecureJson(KEY_FCM_TOKEN, value)
        get() = getSecureJson(KEY_FCM_TOKEN, SetFcmTokenRequest::class.java)?.fcmToken


    private fun <T> putSecureJson(key: String, value: T) {
        secure().apply {
            val json = Gson().toJson(value)
            putString(key, json)
            commit()
        }
    }

    private fun <T> getSecureJson(key: String, clazz: Class<T>): T? = try {
        val json = cryptedPreferences.getString(key, "")
        Gson().fromJson(json, clazz)
    } catch (e: Exception) {
        Timber.w(e, "Could not retrieve $key from cryptedPreferences")
        null
    }

    private fun secure() = cryptedPreferences.edit()

}