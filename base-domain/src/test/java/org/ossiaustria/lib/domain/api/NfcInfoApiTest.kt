package org.ossiaustria.lib.domain.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.ossiaustria.lib.domain.models.NfcInfo
import retrofit2.Retrofit
import java.util.*

class NfcInfoApiTest : AbstractApiTest() {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val idExisting = UUID.randomUUID()

    lateinit var subject: NfcInfoApi

    override fun createApi(retrofit: Retrofit) {
        subject = retrofit.create(NfcInfoApi::class.java)
    }

    @Test
    fun `NfcTagApi get should retrieve one item `() {
        val item = runBlocking {
            subject.getOne(idExisting)
        }
        checkItem(item)
    }

    @Test
    fun `NfcTagApi getAll should retrieve all items `() {
        val items = runBlocking {
            subject.getAllAccessibleNfcs()
        }

        assertNotNull(items)
        assertEquals(items.size, 2)
        items.forEach { checkItem(it) }
    }

    private fun checkItem(item: NfcInfo) {
        assertNotNull(item)
        assertNotNull(item.id)
        assertNotNull(item.creatorId)
        assertNotNull(item.createdAt)
        assertNotNull(item.ownerId)
        assertNotNull(item.type)
        assertNotNull(item.name)
        assertNotNull(item.linkedPersonId)
        assertNotNull(item.linkedAlbumId)
    }

    override fun setupMockingMap(): Map<String, MockResponse> = mapOf(
        "nfcs/$idExisting" to MockResponse(
            JsonMocker.nfc()
        ),
        "nfcs/own" to MockResponse(
            JsonMocker.createList(
                listOf(
                    JsonMocker.nfc(),
                    JsonMocker.nfc()
                )
            )
        )
    )

}