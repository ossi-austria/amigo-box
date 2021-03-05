package org.ossiaustria.lib.domain.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

/**
 * Example API Test
 *
 *  1. Inherit from Abstract test
 *  2. Apply TestRule
 *  3. mock the server response
 */
class PostApiTest : AbstractApiTest<PostApi>(PostApi::class.java) {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `PostApi get must retrieve on Post `() {
        val post = runBlocking {
            subject.get(1)
        }
        assertNotNull(post)
        assertNotNull(post.authorId)
        assertNotNull(post.text)
        assertNotNull(post.id)
        assertNotNull(post.title)
    }

    override fun setupMockingMap(): Map<String, MockResponse> = mapOf(
        "posts/1" to MockResponse(
            """{
                "id":2,
                "authorId":1,
                "title":"title",
                "text":"text"
                }""".trimIndent()
        )
    )

}