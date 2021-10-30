package org.ossiaustria.lib.domain.services

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
    fun myAccount(): Flow<Resource<Account>>

    /**
     * Performs a login on the server with (email, password) and stores the result locally.
     * After login, account, accessToken and refreshToken are stored and authenticated API requests should be possible
     *
     * In case of any error, an Effect.Failure is returned.
     * May support Effect.Loading, but Effect.Success is possible immediately as well
     */
    fun login(email: String, password: String): Flow<Resource<Account>>

    /**
     * Registers a new users on the server with (email, password, fullname).
     *
     * In case of any error, an Effect.Failure is returned.
     * May support Effect.Loading, but Effect.Success is possible immediately as well
     */
    fun register(email: String, password: String, name: String): Flow<Resource<Account>>

    /**
     * Refreshes the accessToken with a pre-existing freshedToken (exists after login)
     * After login, account, accessToken and refreshToken are stored and authenticated API requests should be possible
     *
     * In case of any error, an Effect.Failure is returned.
     * May support Effect.Loading, but Effect.Success is possible immediately as well
     */
    fun refreshAccessToken(): Flow<Resource<TokenResult>>

    fun setFcmToken(fcmToken: String): Flow<Resource<Boolean>>
}

interface LoginCleanupService {
    suspend fun cleanup()
}

class AuthServiceImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val authApi: AuthApi,
    private val settingsRepository: SettingsRepository,
    private val userContext: UserContext,
    private val loginCleanupService: LoginCleanupService,
) : AuthService {

    override fun login(email: String, password: String): Flow<Resource<Account>> {
        return flow {
            emit(Resource.loading())
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

            emit(Resource.success(result.account))
        }.catch {
            Timber.e(it, "Could not login")
            emit(Resource.failure(it))
        }.flowOn(ioDispatcher)
    }

    override fun register(email: String, password: String, name: String): Flow<Resource<Account>> {
        return flow {
            emit(Resource.loading())
            val account = authApi.register(RegisterRequest(email, password, name))
            emit(Resource.success(account))
        }.catch { e ->
            Timber.e(e, "Could not refreshAccessToken")
            emit(Resource.failure(e))
        }.flowOn(ioDispatcher)
    }

    override fun refreshAccessToken(): Flow<Resource<TokenResult>> {
        return flow<Resource<TokenResult>> {
            val refreshToken = settingsRepository.refreshToken
            if (refreshToken == null) {
                emit(Resource.failure("No local refreshToken found!"))
            } else {
                emit(Resource.loading())
                val accessToken = authApi.refreshToken(RefreshTokenRequest(refreshToken.token))
                settingsRepository.accessToken = accessToken
                emit(Resource.success(accessToken))
            }
        }.catch { e ->
            Timber.e(e, "Could not refreshAccessToken")
            emit(Resource.failure(e))
        }.flowOn(ioDispatcher)
    }

    override fun myAccount(): Flow<Resource<Account>> {
        return flow {
            emit(Resource.loading())
            val account = authApi.whoami()
            settingsRepository.account = account
            emit(Resource.success(account))
        }.catch { e ->
            Timber.e(e, "Could not myAccount")
            emit(Resource.failure(e))
        }.flowOn(ioDispatcher)
    }

    override fun setFcmToken(fcmToken: String): Flow<Resource<Boolean>> {
        return flow<Resource<Boolean>> {
            val account = settingsRepository.account
            if (account == null) {
                emit(Resource.failure("No local account found!"))
            } else {
                emit(Resource.loading())
                settingsRepository.fcmToken = fcmToken
                authApi.setFcmToken(SetFcmTokenRequest(fcmToken))

                emit(Resource.success(true))

            }
        }.catch { e ->
            Timber.e(e, "Could not setFcmToken")
            emit(Resource.failure(e))
        }.flowOn(ioDispatcher)
    }
}