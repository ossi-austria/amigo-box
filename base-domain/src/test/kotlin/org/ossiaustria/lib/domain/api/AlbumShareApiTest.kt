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


class AlbumShareApiTest : AbstractApiTest() {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val idExisting = UUID.randomUUID()

    lateinit var subject: AlbumShareApi

    override fun createApi(retrofit: Retrofit) {
        subject = retrofit.create(AlbumShareApi::class.java)
    }

    @Test
    fun `AlbumShareApi get should retrieve one item `() {
        val item = runBlocking {
            subject.get(idExisting)
        }
        assertNotNull(item)
        assertNotNull(item.id)
        assertNotNull(item.sentAt)
        assertNotNull(item.createdAt)
        assertNotNull(item.retrievedAt)
        assertNotNull(item.senderId)
        assertNotNull(item.receiverId)
        assertNotNull(item.album)
    }

    @Test
    fun `AlbumShareApi getAll should retrieve all items `() {
        val items = runBlocking {
            subject.getAll()
        }

        assertNotNull(items)
        assertEquals(items.size, 2)

    }

    override fun setupMockingMap(): Map<String, MockResponse> = mapOf(
        "shares/$idExisting" to MockResponse(
            JsonMocker.share()
        ),
        "shares" to MockResponse(
            JsonMocker.createList(
                listOf(
                    JsonMocker.share(),
                    JsonMocker.share()
                )
            )
        )
    )

}