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


class MultimediaApiTest : AbstractApiTest() {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val idExisting = UUID.randomUUID()

    lateinit var subject: MultimediaApi

    override fun createApi(retrofit: Retrofit) {
        subject = retrofit.create(MultimediaApi::class.java)
    }

    @Test
    fun `MultimediaApi get should retrieve one item `() {
        val item = runBlocking {
            subject.getOne(idExisting)
        }
        assertNotNull(item)
        assertNotNull(item.id)
        assertNotNull(item.createdAt)
        assertNotNull(item.ownerId)
        assertNotNull(item.filename)
        assertNotNull(item.contentType)
        assertNotNull(item.type)
        assertNotNull(item.size)
//        assertNotNull(item.albumId)
    }

    @Test
    fun `MultimediaApi getAll should retrieve all items `() {
        val items = runBlocking {
            subject.getShared()
        }

        assertNotNull(items)
        assertEquals(items.size, 2)

    }

    override fun setupMockingMap(): Map<String, MockResponse> = mapOf(
        "multimedias/$idExisting" to MockResponse(
            JsonMocker.multimedia()
        ),
        "multimedias/shared" to MockResponse(
            JsonMocker.createList(
                listOf(
                    JsonMocker.multimedia(),
                    JsonMocker.multimedia()
                )
            )
        )
    )

}