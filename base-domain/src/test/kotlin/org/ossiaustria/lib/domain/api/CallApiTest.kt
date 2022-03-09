package org.ossiaustria.lib.domain.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import retrofit2.Retrofit
import java.util.*


class CallApiTest : AbstractApiTest() {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val idExisting = UUID.randomUUID()

    lateinit var subject: CallApi

    override fun createApi(retrofit: Retrofit) {
        subject = retrofit.create(CallApi::class.java)
    }

    @Test
    fun `CallApi get should retrieve one item `() {
        val item = runBlocking {
            subject.getOne(idExisting)
        }
        assertNotNull(item)
        assertNotNull(item.id)
        assertNotNull(item.sentAt)
        assertNotNull(item.createdAt)
        assertNotNull(item.retrievedAt)
        assertNotNull(item.senderId)
        assertNotNull(item.receiverId)
        assertNotNull(item.callType)
    }

    @Test
    fun `CallApi getAll should retrieve all items `() {
        val items = runBlocking {
            subject.getOwn()
        }

        assertNotNull(items)
        assertEquals(items.size, 2)

    }

    override fun setupMockingMap(): Map<String, MockResponse> = mapOf(
        "calls/$idExisting" to MockResponse(
            JsonMocker.call()
        ),
        "calls/all" to MockResponse(
            JsonMocker.createList(
                listOf(
                    JsonMocker.call(),
                    JsonMocker.call()
                )
            )
        )
    )

}