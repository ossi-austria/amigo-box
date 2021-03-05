package org.ossiaustria.lib.domain.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *  AbstractApiTest: mock responses and test Apis
 *
 * use the setupMockingMap() method to describe the responses to use in test
 */
abstract class AbstractApiTest<T : Api>(private val clazz: Class<T>) {

    protected lateinit var subject: T

    private lateinit var mockingInterceptor: DebugMockInterceptor

    @Before
    fun createRetrofit() {
        val rootUrl = "http://localhost/"

        val mockingMap = setupMockingMap()
        mockingInterceptor = DebugMockInterceptor(mockingMap)

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        // inject a fake response server into the httpClient
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