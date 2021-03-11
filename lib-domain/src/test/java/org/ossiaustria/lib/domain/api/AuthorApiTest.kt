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
class AuthorApiTest : AbstractApiTest<AuthorApi>(AuthorApi::class.java) {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `AuthorApi get must retrieve on Post `() {
        val author = runBlocking {
            subject.get(1)
        }
        assertNotNull(author)
        assertNotNull(author.name)
        assertNotNull(author.id)
    }

    override fun setupMockingMap(): Map<String, MockResponse> = mapOf(
        "authors/1" to MockResponse(
            """{
                "id":2,
                "name":"name",
                }""".trimIndent()
        )
    )

}