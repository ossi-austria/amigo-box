package org.ossiaustria.lib.domain.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.ossiaustria.lib.domain.auth.AuthInterceptor
import java.util.concurrent.TimeUnit

class OkHttpBuilder {

    companion object {
        const val CONNECT_TIMEOUT = 10L
        const val WRITE_TIMEOUT = 1L
        const val READ_TIMEOUT = 20L
        const val BASE_URL = "http://amigo-dev.ossi-austria.org:8080/v1/"

    }

    fun build(
        mockInterceptor: MockInterceptor,
        provideAuthInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            networkInterceptors().add(Interceptor {
                val requestBuilder: Request.Builder = it.request().newBuilder()
                requestBuilder
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")

                val currentToken = provideAuthInterceptor.currentToken()
                currentToken?.let {
                    val token = it.token
                    requestBuilder.header("Authorization", "Bearer $token")
                }
                it.proceed(requestBuilder.build())
            })
            addInterceptor(mockInterceptor)
        }.build()
    }
}