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


class NfcTagApiTest : AbstractApiTest() {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val idExisting = UUID.randomUUID()

    lateinit var subject: NfcTagApi

    override fun createApi(retrofit: Retrofit) {
        subject = retrofit.create(NfcTagApi::class.java)
    }

    @Test
    fun `NfcTagApi get should retrieve one item `() {
        val item = runBlocking {
            subject.get(idExisting)
        }
        assertNotNull(item)
        assertNotNull(item.id)
        assertNotNull(item.creatorId)
        assertNotNull(item.createdAt)
        assertNotNull(item.ownerId)
        assertNotNull(item.type)
        assertNotNull(item.linkedPersonId)
        assertNotNull(item.linkedMediaId)
        assertNotNull(item.linkedAlbumId)
    }

    @Test
    fun `NfcTagApi getAll should retrieve all items `() {
        val items = runBlocking {
            subject.getAll()
        }

        assertNotNull(items)
        assertEquals(items.size, 2)

    }

    override fun setupMockingMap(): Map<String, MockResponse> = mapOf(
        "nfcs/$idExisting" to MockResponse(
            JsonMocker.nfc()
        ),
        "nfcs" to MockResponse(
            JsonMocker.createList(
                listOf(
                    JsonMocker.nfc(),
                    JsonMocker.nfc()
                )
            )
        )
    )

}