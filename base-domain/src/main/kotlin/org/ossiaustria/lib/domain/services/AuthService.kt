package org.ossiaustria.lib.domain.services

import org.ossiaustria.lib.domain.auth.Account
import org.ossiaustria.lib.domain.auth.AuthApi
import org.ossiaustria.lib.domain.auth.LoginRequest
import org.ossiaustria.lib.domain.auth.RefreshTokenRequest
import org.ossiaustria.lib.domain.auth.RegisterRequest
import org.ossiaustria.lib.domain.auth.SetFcmTokenRequest
import org.ossiaustria.lib.domain.auth.TokenResult
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.modules.UserContext
import org.ossiaustria.lib.domain.repositories.SettingsRepository
import timber.log.Timber

/**
 * Central Service to handle authentication and user requests to the server and store locally
 */
interface AuthService {

    /**
     * Updates the locally stored Account. Can be used anytime, but needs to be authenticated
     *
     * In case of any error, an Effect.Failure is returned.
     * May support Effect.Loading, but Effect.Success is possible immediately as well
     */
    suspend fun myAccount(): Resource<Account>

    /**
     * Performs a login on the server with (email, password) and stores the result locally.
     * After login, account, accessToken and refreshToken are stored and authenticated API requests should be possible
     *
     * In case of any error, an Effect.Failure is returned.
     * May support Effect.Loading, but Effect.Success is possible immediately as well
     */
    suspend fun login(email: String, password: String): Resource<Account>

    suspend fun logout(): Resource<Boolean>

    /**
     * Registers a new users on the server with (email, password, fullname).
     *
     * In case of any error, an Effect.Failure is returned.
     * May support Effect.Loading, but Effect.Success is possible immediately as well
     */
    suspend fun register(email: String, password: String, name: String): Resource<Account>

    /**
     * Refreshes the accessToken with a pre-existing freshedToken (exists after login)
     * After login, account, accessToken and refreshToken are stored and authenticated API requests should be possible
     *
     * In case of any error, an Effect.Failure is returned.
     * May support Effect.Loading, but Effect.Success is possible immediately as well
     */
    suspend fun refreshAccessToken(): Resource<TokenResult>

    suspend fun setFcmToken(fcmToken: String): Resource<Boolean>
}

interface LoginCleanupService {
    suspend fun cleanup()
}

class AuthServiceImpl(
    private val authApi: AuthApi,
    private val settingsRepository: SettingsRepository,
    private val userContext: UserContext,
    private val loginCleanupService: LoginCleanupService,
) : AuthService {

    override suspend fun login(email: String, password: String): Resource<Account> = try {
        settingsRepository.accessToken = null

        val result = authApi.login(LoginRequest(email = email, password = password))

        // delete local stored data!!
        loginCleanupService.cleanup()
        settingsRepository.account = result.account
        settingsRepository.refreshToken = result.refreshToken
        settingsRepository.accessToken = result.accessToken

        result.account.persons.lastOrNull()?.let {
            settingsRepository.currentPerson = it
            settingsRepository.currentPersonId = it.id
        }

        userContext.initContext(
            settingsRepository.accessToken,
            settingsRepository.account,
            settingsRepository.currentPerson
        )

        Resource.success(result.account)
    } catch (e: Exception) {
        Timber.e(e, "Could not login")
        Resource.failure(e)

    }

    override suspend fun logout(): Resource<Boolean> = try {

        if (userContext.available()) {
            loginCleanupService.cleanup()
            settingsRepository.fcmToken = null
            settingsRepository.account = null
            settingsRepository.currentPerson = null
            settingsRepository.currentPersonId = null
            settingsRepository.refreshToken = null
            settingsRepository.accessToken = null
            userContext.initContext(null, null, null)
            Resource.success(true)
        } else {
            Resource.success(false)
        }
    } catch (e: Exception) {
        Timber.e(e, "Could not refreshAccessToken")
        Resource.failure(e)
    }

    override suspend fun register(
        email: String,
        password: String,
        name: String
    ): Resource<Account> =
        try {
            val account = authApi.register(RegisterRequest(email, password, name))
            Resource.success(account)
        } catch (e: Exception) {
            Timber.e(e, "Could not refreshAccessToken")
            Resource.failure(e)
        }

    override suspend fun refreshAccessToken(): Resource<TokenResult> = try {

        val refreshToken = settingsRepository.refreshToken
        if (refreshToken == null) {
            Resource.failure("No local refreshToken found!")
        } else {

            val accessToken = authApi.refreshToken(RefreshTokenRequest(refreshToken.token))
            settingsRepository.accessToken = accessToken
            Resource.success(accessToken)
        }
    } catch (e: Exception) {
        Timber.e(e, "Could not refreshAccessToken")
        Resource.failure(e)

    }

    override suspend fun myAccount(): Resource<Account> = try {
        val account = authApi.whoami()
        settingsRepository.account = account
        Resource.success(account)
    } catch (e: Exception) {
        Timber.e(e, "Could not myAccount")
        Resource.failure(e)
    }

    override suspend fun setFcmToken(fcmToken: String): Resource<Boolean> = try {
        val account = settingsRepository.account
        if (account == null) {
            Resource.failure("No local account found!")
        } else {
            settingsRepository.fcmToken = fcmToken
            authApi.setFcmToken(SetFcmTokenRequest(fcmToken))
            Resource.success(true)
        }
    } catch (e: Exception) {
        Timber.e(e, "Could not setFcmToken")
        Resource.failure(e)
    }
}