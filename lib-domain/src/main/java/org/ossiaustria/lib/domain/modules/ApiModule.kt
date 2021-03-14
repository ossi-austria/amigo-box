package org.ossiaustria.lib.domain.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.ossiaustria.lib.domain.api.AlbumApi
import org.ossiaustria.lib.domain.api.AuthorApi
import org.ossiaustria.lib.domain.api.DebugMockInterceptor
import org.ossiaustria.lib.domain.api.DebugMockInterceptorAdapter
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
    private const val BASE_URL = "http://hp-api.herokuapp.com/"

    @Provides
    @Singleton
    internal fun provideOkHttpClient(debugMockInterceptor: DebugMockInterceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            addInterceptor(debugMockInterceptor)
        }.build()
    }

    @Provides
    @Singleton
    internal fun debugMockInterceptor(): DebugMockInterceptor {
        return DebugMockInterceptor(emptyMap(), DebugMockInterceptorAdapter())
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
    internal fun authorApi(retrofit: Retrofit): AuthorApi {
        return retrofit.create(AuthorApi::class.java)
    }

    @Provides
    @Singleton
    internal fun albumApi(retrofit: Retrofit): AlbumApi {
        return retrofit.create(AlbumApi::class.java)
    }

}