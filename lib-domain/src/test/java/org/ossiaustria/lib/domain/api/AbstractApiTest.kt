package org.ossiaustria.lib.domain.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


abstract class AbstractApiTest<T : Api>(private val clazz: Class<T>) {

    protected lateinit var subject: T

    protected lateinit var mockingInterceptor: DebugMockInterceptor

    @Before
    fun createRetrofit() {
        val rootUrl = "http://localhost/"

        val mockingMap = setupMockingMap()
        mockingInterceptor = DebugMockInterceptor(mockingMap)

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(mockingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(rootUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        subject = retrofit.create(clazz)
    }

    abstract fun setupMockingMap(): Map<String, MockResponse>
}