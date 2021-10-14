package org.ossiaustria.lib.domain.auth

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResult

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Account

    @POST("auth/refresh-token")
    suspend fun refreshToken(@Body refreshToken: RefreshTokenRequest): TokenResult

    @GET("auth/whoami")
    suspend fun whoami(): Account

    @POST("account/set-fcm-token")
    suspend fun setFcmToken(@Body setFcmTokenRequest: SetFcmTokenRequest)
}

data class LoginRequest(
    val email: String,
    val password: String,
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
)