package org.ossiaustria.lib.domain.example.api

import okhttp3.*
import org.ossiaustria.lib.domain.BuildConfig
import org.ossiaustria.lib.domain.example.api.DebugMockInterceptor.Companion.DELAY_DEFAULT
import timber.log.Timber


data class MockResponse(
    val content: String,
    val status: Int = 200,
    val contentType: String = "application/json",
    val delay: Long = DELAY_DEFAULT
)

/**
 * This will help us to test our networking code while a particular API is not implemented
 * yet on Backend side.
 */
class DebugMockInterceptor(private val requestContentMap: Map<String, MockResponse>) : Interceptor {

    companion object {
        const val DELAY_DEFAULT = 100L
        const val DELAY_MAX = 60 * 1000L
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            val uri = chain.request().url().uri().toString()

            val matchedKeys = requestContentMap.keys.filter { uri.endsWith(it) }
            if (matchedKeys.isEmpty()) throw IllegalStateException("No key found for url: uri")
            if (matchedKeys.size > 1) Timber.w("More than one mocking candidate found: $matchedKeys")

            val matchedKey = matchedKeys.first()
            val mockResponse = requestContentMap[matchedKey] ?: error("No key found for url: uri")

            if (mockResponse.delay < DELAY_MAX) {
                Timber.d("Imitating blocking request for ${mockResponse.delay} ms")
                Thread.sleep(mockResponse.delay)
            }
            return Response.Builder().request(chain.request())
//            return chain.proceed(chain.request()).newBuilder()

                .code(mockResponse.status)
                .protocol(Protocol.HTTP_2)
                .message(mockResponse.content)
                .body(
                    ResponseBody.create(
                        MediaType.parse(mockResponse.contentType),
                        mockResponse.content.toByteArray()
                    )
                )
                .addHeader("content-type", mockResponse.contentType)
                .build()
        } else {
            //just to be on safe side.
            throw IllegalAccessError(
                "MockInterceptor is only meant for Testing Purposes and " +
                        "bound to be used only with DEBUG mode"
            )
        }
    }

}