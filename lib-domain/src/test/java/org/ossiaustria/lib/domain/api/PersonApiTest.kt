package org.ossiaustria.lib.domain.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import retrofit2.Retrofit
import java.util.*

/**
 * Example API Test
 *
 *  1. Inherit from Abstract test
 *  2. Apply TestRule
 *  3. mock the server response
 */
class PersonApiTest : AbstractApiTest() {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val idExisting = UUID.randomUUID()

    lateinit var subject: PersonApi

    override fun createApi(retrofit: Retrofit) {
        subject = retrofit.create(PersonApi::class.java)
    }

    @Test
    fun `PersonApi get should retrieve one item `() {
        val person = runBlocking {
            subject.get(idExisting)
        }
        assertNotNull(person)
        assertNotNull(person.name)
        assertNotNull(person.id)
        assertNotNull(person.email)
        assertNotNull(person.memberType)
        assertNotNull(person.groupId)

    }

    @Test
    fun `PersonApi getAll should retrieve all items `() {
        val items = runBlocking {
            subject.getAll()
        }

        assertNotNull(items)
        Assert.assertEquals(items.size, 2)
    }

    override fun setupMockingMap(): Map<String, MockResponse> = mapOf(
        "persons/$idExisting" to MockResponse(
            JsonMocker.person(id = idExisting,)
        ),
        "persons" to MockResponse(
            JsonMocker.createList(
                listOf(
                    JsonMocker.person(),
                    JsonMocker.person()
                )
            )
        )
    )

}