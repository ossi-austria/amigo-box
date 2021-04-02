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


class MessageApiTest : AbstractApiTest() {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val idExisting = UUID.randomUUID()

    lateinit var subject: MessageApi

    override fun createApi(retrofit: Retrofit) {
        subject = retrofit.create(MessageApi::class.java)
    }

    @Test
    fun `MessageApi get should retrieve one item `() {
        val item = runBlocking {
            subject.get(idExisting)
        }
        assertNotNull(item)
        assertNotNull(item.id)
        assertNotNull(item.sendAt)
        assertNotNull(item.createdAt)
        assertNotNull(item.retrievedAt)
        assertNotNull(item.senderId)
        assertNotNull(item.receiverId)
        assertNotNull(item.text)
    }

    @Test
    fun `MessageApi getAll should retrieve all items `() {
        val items = runBlocking {
            subject.getAll()
        }

        assertNotNull(items)
        assertEquals(items.size, 2)

    }

    override fun setupMockingMap(): Map<String, MockResponse> = mapOf(
        "messages/$idExisting" to MockResponse(
            JsonMocker.message()
        ),
        "messages" to MockResponse(
            JsonMocker.createList(
                listOf(
                    JsonMocker.message(),
                    JsonMocker.message()
                )
            )
        )
    )

}