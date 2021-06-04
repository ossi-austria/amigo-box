package org.ossiaustria.lib.domain.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.ossiaustria.lib.domain.api.AlbumApi
import org.ossiaustria.lib.domain.api.AlbumShareApi
import org.ossiaustria.lib.domain.api.CallApi
import org.ossiaustria.lib.domain.api.GroupApi
import org.ossiaustria.lib.domain.api.MessageApi
import org.ossiaustria.lib.domain.api.MockInterceptor
import org.ossiaustria.lib.domain.api.MultimediaApi
import org.ossiaustria.lib.domain.api.NfcTagApi
import org.ossiaustria.lib.domain.api.NoopMockInterceptor
import org.ossiaustria.lib.domain.api.PersonApi
import org.ossiaustria.lib.domain.auth.AuthApi
import org.ossiaustria.lib.domain.auth.AuthInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private const val CONNECT_TIMEOUT = 10L
    private const val WRITE_TIMEOUT = 1L
    private const val READ_TIMEOUT = 20L
    private const val BASE_URL = "http://192.168.0.30:8080/v1/"

    @Provides
    @Singleton
    internal fun provideAuthInterceptor(): AuthInterceptor {
        return AuthInterceptor()
    }

    @Provides
    @Singleton
    internal fun provideUserContext(): UserContext {
        return UserContext()
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(
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
                requestBuilder.header("Content-Type", "application/json")

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

    @Provides
    @Singleton
    internal fun debugMockInterceptor(): MockInterceptor {
        return NoopMockInterceptor() //DebugMockInterceptor(emptyMap(), DebugMockInterceptorAdapter())
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    internal fun groupApi(retrofit: Retrofit): GroupApi {
        return retrofit.create(GroupApi::class.java)
    }

    @Provides
    @Singleton
    internal fun authApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    internal fun albumApi(retrofit: Retrofit): AlbumApi {
        return retrofit.create(AlbumApi::class.java)
    }

    @Provides
    @Singleton
    internal fun albumShareApi(retrofit: Retrofit): AlbumShareApi {
        return retrofit.create(AlbumShareApi::class.java)
    }

    @Provides
    @Singleton
    internal fun callApi(retrofit: Retrofit): CallApi {
        return retrofit.create(CallApi::class.java)
    }

    @Provides
    @Singleton
    internal fun multimediaApi(retrofit: Retrofit): MultimediaApi {
        return retrofit.create(MultimediaApi::class.java)
    }


    @Provides
    @Singleton
    internal fun messageApi(retrofit: Retrofit): MessageApi {
        return retrofit.create(MessageApi::class.java)
    }

    @Provides
    @Singleton
    internal fun nfcTagApi(retrofit: Retrofit): NfcTagApi {
        return retrofit.create(NfcTagApi::class.java)
    }

    @Provides
    @Singleton
    internal fun personApi(retrofit: Retrofit): PersonApi {
        return retrofit.create(PersonApi::class.java)
    }
}