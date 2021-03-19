package org.ossiaustria.lib.domain.api

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.ossiaustria.lib.domain.BuildConfig
import org.ossiaustria.lib.domain.api.DebugMockInterceptor.Companion.DELAY_DEFAULT
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
class DebugMockInterceptor(
    private val requestContentMap: Map<String, MockResponse>,
    private val debugMockInterceptorAdapter: DebugMockInterceptorAdapter? = null
) : Interceptor {

    companion object {
        const val DELAY_DEFAULT = 100L
        const val DELAY_MAX = 60 * 1000L
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            val uri = chain.request().url.toUri().toString()
            val mockLiveResponse = debugMockInterceptorAdapter?.requestUrl(uri)

            val mockResponse = if (mockLiveResponse == null) {
                val matchedKeys = requestContentMap.keys.filter { uri.endsWith(it) }
                if (matchedKeys.isEmpty()) throw IllegalStateException("No key found for url: $uri")
                if (matchedKeys.size > 1) Timber.w("More than one mocking candidate found: $matchedKeys")
                val matchedKey = matchedKeys.first()

                requestContentMap[matchedKey] ?: error("No key found for url: $uri")
            } else {
                mockLiveResponse
            }

            if (mockResponse.delay < DELAY_MAX) {
                Timber.d("Imitating blocking request for ${mockResponse.delay} ms")
                Thread.sleep(mockResponse.delay)
            }
            return Response.Builder().request(chain.request())

                .code(mockResponse.status)
                .protocol(Protocol.HTTP_2)
                .message(mockResponse.content)
                .body(
                    mockResponse.content.toByteArray()
                        .toResponseBody(mockResponse.contentType.toMediaTypeOrNull())
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