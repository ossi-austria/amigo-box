package org.ossiaustria.lib.domain.repositories

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.FakeAndroidKeyStore
import org.ossiaustria.lib.domain.auth.Account
import org.ossiaustria.lib.domain.auth.TokenResult
import org.ossiaustria.lib.domain.repositories.SettingsRepository.Companion.KEY_ACCESS_TOKEN
import org.ossiaustria.lib.domain.repositories.SettingsRepository.Companion.KEY_ACCOUNT
import org.ossiaustria.lib.domain.repositories.SettingsRepository.Companion.KEY_REFRESH_TOKEN
import org.robolectric.RobolectricTestRunner
import java.util.*
import java.util.UUID.randomUUID

@RunWith(RobolectricTestRunner::class)
internal class SettingsRepositoryTest() {

    lateinit var subject: SettingsRepositoryImpl

    @Before
    fun before() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        FakeAndroidKeyStore.setup

        val cryptPrefs = EncryptedSharedPreferences.create(
            SettingsRepository.SETTINGS_AMIGO_CRYPTED,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        subject = SettingsRepositoryImpl(context, cryptPrefs)
    }

    // IMPORTANT: Can fail on windows!
    @Test
    fun `should write and read TokenResult accessToken from crypted store`() {
        val tokenResult = TokenResult("token", "subject", Date(), Date(), "issuer")
        subject.accessToken = tokenResult
        val result = subject.accessToken
        assertNotNull(result)
        assertEquals(tokenResult.token, result!!.token)
        assertEquals(tokenResult.subject, result.subject)
        assertEquals(tokenResult.issuer, result.issuer)

        assertEquals("NONE", subject.sharedPreferences.getString(KEY_ACCESS_TOKEN, "NONE"))
        assertNotEquals("NONE", subject.cryptedPreferences.getString(KEY_ACCESS_TOKEN, "NONE"))
    }

    // IMPORTANT: Can fail on windows!
    @Test
    fun `should write and read TokenResult refreshToken from crypted store`() {
        val tokenResult = TokenResult("token", "subject", Date(), Date(), "issuer")
        subject.refreshToken = tokenResult
        val result = subject.refreshToken
        assertNotNull(result)
        assertEquals(tokenResult.token, result!!.token)
        assertEquals(tokenResult.subject, result.subject)
        assertEquals(tokenResult.issuer, result.issuer)

        assertEquals("NONE", subject.sharedPreferences.getString(KEY_REFRESH_TOKEN, "NONE"))
        assertNotEquals("NONE", subject.cryptedPreferences.getString(KEY_REFRESH_TOKEN, "NONE"))
    }

    // IMPORTANT: Can fail on windows!
    @Test
    fun `should write and read Account from crypted store`() {
        val account = Account(randomUUID(), "email@example.org", "", Date())
        subject.account = account
        val result = subject.account
        assertNotNull(result)
        assertEquals(account.id, result!!.id)
        assertEquals(account.email, result.email)

        assertEquals("NONE", subject.sharedPreferences.getString(KEY_ACCOUNT, "NONE"))
        assertNotEquals("NONE", subject.cryptedPreferences.getString(KEY_ACCOUNT, "NONE"))
    }
}
