package org.ossiaustria.lib.domain.services

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.ossiaustria.lib.commons.testing.TestCoroutineRule
import org.ossiaustria.lib.domain.EntityMocks
import org.ossiaustria.lib.domain.auth.Account
import org.ossiaustria.lib.domain.auth.AuthApi
import org.ossiaustria.lib.domain.auth.AuthInterceptor
import org.ossiaustria.lib.domain.auth.LoginRequest
import org.ossiaustria.lib.domain.auth.LoginResult
import org.ossiaustria.lib.domain.auth.RefreshTokenRequest
import org.ossiaustria.lib.domain.auth.RegisterRequest
import org.ossiaustria.lib.domain.auth.TokenResult
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.modules.UserContext
import org.ossiaustria.lib.domain.repositories.SettingsRepository
import java.util.*

@ExperimentalCoroutinesApi
class AuthServiceTest {

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @MockK
    lateinit var authApi: AuthApi

    @MockK
    lateinit var settingsRepository: SettingsRepository

    @MockK
    lateinit var userContext: UserContext

    lateinit var authService: AuthService

    private lateinit var loginResult: LoginResult
    private lateinit var account: Account

    @Before
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        authService =
            AuthServiceImpl(coroutineRule.dispatcher, authApi, settingsRepository, userContext)

        account = EntityMocks.account(email = "test@example.org")
        loginResult = LoginResult(
            account,
            EntityMocks.tokenResult(),
            EntityMocks.tokenResult()
        )

        coEvery {
            authApi.login(any())
        } throws IllegalArgumentException("RestReception")

        coEvery {
            authApi.login(eq(LoginRequest("test@example.org", "password")))
        } returns loginResult

        coEvery { authApi.register(any()) } throws IllegalArgumentException("RestReception")

        coEvery {
            authApi.register(eq(RegisterRequest("test@example.org", "password", "Full name")))
        } returns account

        every { settingsRepository.accessToken } returns null
        every { settingsRepository.account   } returns null
        every { settingsRepository.currentPerson } returns null
    }

    @Test
    fun `login should return Account when credentials are valid`() =
        runBlockingTest(coroutineRule.dispatcher) {
            val result = authService.login("test@example.org", "password").first { !it.isLoading }
            assertTrue(result is Resource.Success)
            assertEquals(account, result.valueOrNull())
        }

    @Test
    fun `login should store account and tokens when credentials are valid`() =
        runBlockingTest(coroutineRule.dispatcher) {
            authService.login("test@example.org", "password").first { !it.isLoading }

            verify { settingsRepository.account = eq(loginResult.account) }
            verify { settingsRepository.refreshToken = any() }
            verify { settingsRepository.accessToken = any() }
        }

    @Test
    fun `login should return FAILURE when credentials are invalid`() =
        runBlockingTest(coroutineRule.dispatcher) {
            val result = authService.login("test@example.org", "wrong").first { !it.isLoading }
            assertNotNull(result)
            assertTrue(result is Resource.Failure)
        }

    @Test
    fun `register should return Account when new account was created`() =
        runBlockingTest(coroutineRule.dispatcher) {
            val result = authService.register("test@example.org", "password", "Full name")
                .first { !it.isLoading }
            assertTrue(result is Resource.Success)
            assertEquals(account, result.valueOrNull())
        }

    @Test
    fun `register should return FAILURE when register request failed`() =
        runBlockingTest(coroutineRule.dispatcher) {
            val result =
                authService.register("test@example.org", "", "Full name").first { !it.isLoading }
            assertNotNull(result)
            assertTrue(result is Resource.Failure)

        }

    @Test
    fun `refreshAccessToken should return TokenResult when refresh was possible`() =
        runBlockingTest(coroutineRule.dispatcher) {

            coEvery { settingsRepository.refreshToken } returns EntityMocks.tokenResult()
            val mock = TokenResult("token", "subject", Date(), Date(), "issuer")
            coEvery {
                authApi.refreshToken(eq(RefreshTokenRequest("token")))
            } returns mock

            val result = authService.refreshAccessToken().first { !it.isLoading }
            assertTrue(result is Resource.Success)
            assertEquals(mock, result.valueOrNull())
            verify { settingsRepository.accessToken = eq(mock) }
        }

    @Test
    fun `refreshAccessToken should return FAILURE when no local refresh token available`() =
        runBlockingTest(coroutineRule.dispatcher) {
            coEvery { settingsRepository.refreshToken } returns null

            val result = authService.refreshAccessToken().first { !it.isLoading }
            assertTrue(result is Resource.Failure)
        }

    @Test
    fun `refreshAccessToken should return FAILURE when refresh failed`() =
        runBlockingTest(coroutineRule.dispatcher) {
            coEvery { authApi.refreshToken(any()) } throws IllegalArgumentException("RestReception")

            val result = authService.refreshAccessToken().first { !it.isLoading }
            assertTrue(result is Resource.Failure)
        }

    @Test
    fun `myAccount should return Account when credentials are valid`() =
        runBlockingTest(coroutineRule.dispatcher) {

            coEvery { authApi.whoami() } returns account

            val result = authService.myAccount().first { !it.isLoading }
            assertTrue(result is Resource.Success)
            assertEquals(account, result.valueOrNull())
            verify { settingsRepository.account = eq(account) }
        }

    @Test
    fun `myAccount should return FAILURE when unauthenticated`() =
        runBlockingTest(coroutineRule.dispatcher) {
            coEvery { authApi.whoami() } throws IllegalArgumentException("RestReception")
            val result = authService.myAccount().first { !it.isLoading }
            assertNotNull(result)
            assertTrue(result is Resource.Failure)
        }
}