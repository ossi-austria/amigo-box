package org.ossiaustria.lib.domain.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.ossiaustria.lib.domain.BuildConfig
import org.ossiaustria.lib.domain.auth.AuthInterceptor
import timber.log.Timber
import java.util.concurrent.TimeUnit

class OkHttpBuilder {

    companion object {
        const val CONNECT_TIMEOUT = 10L
        const val WRITE_TIMEOUT = 1L
        const val READ_TIMEOUT = 20L
        const val BASE_URL = BuildConfig.API_ENDPOINT
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
                val currentPerson = provideAuthInterceptor.personId()
                currentToken?.let {
                    requestBuilder.header("Authorization", "Bearer ${it.token}")
                }
                currentPerson?.let {
                    requestBuilder.header("Amigo-Person-Id", "$currentPerson")
                }
                val response = it.proceed(requestBuilder.build())
                if (response.isSuccessful) {
                    response
                } else {
                    Timber.w("HTTP-ERROR: ${response.code} <-- ${response.request.method} ${response.request.url}")
                    Timber.w("HTTP-ERROR: ${response.peekBody(100).string()}")
                    response
                }
            })
            addInterceptor(mockInterceptor)
        }.build()
    }
}