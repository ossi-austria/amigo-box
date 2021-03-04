package org.ossiaustria.lib.domain.api

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.ossiaustria.lib.domain.api.MockResponse

class PostApiTest : AbstractApiTest<PostApi>(PostApi::class.java) {


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