package org.ossiaustria.lib.domain.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.*

/**
 * Example API Test
 *
 *  1. Inherit from Abstract test
 *  2. Apply TestRule
 *  3. mock the server response
 */
class AlbumApiTest : AbstractApiTest<AlbumApi>(AlbumApi::class.java) {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val idExisting = UUID.randomUUID()

    @Test
    fun `AlbumApi get should retrieve one item `() {
        val album = runBlocking {
            subject.get(idExisting)
        }
        assertNotNull(album)
        assertNotNull(album.name)
        assertNotNull(album.id)
        assertNotNull(album.createdAt)
        assertNotNull(album.updatedAt)
        assertNotNull(album.ownerId)
        assertNotNull(album.items)
        assertSame(1, album.items.size)

        album.items.forEach {
            assertNotNull(it.id)
            assertNotNull(it.createdAt)
            assertNotNull(it.sendAt)
            assertNotNull(it.retrievedAt)
            assertNotNull(it.sendAt)
            assertNotNull(it.senderId)
            assertNotNull(it.receiverId)
            assertNotNull(it.remoteUrl)
            assertNotNull(it.localUrl)
            assertNotNull(it.type)
            assertNotNull(it.size)
            assertNotNull(it.albumId)
        }

    }

    override fun setupMockingMap(): Map<String, MockResponse> = mapOf(
        "albums/$idExisting" to MockResponse(
            JsonMocker.album(
                id = idExisting,
                multimediaMock = listOf(JsonMocker.multimedia(albumId = idExisting),)
            )
        )
    )

}