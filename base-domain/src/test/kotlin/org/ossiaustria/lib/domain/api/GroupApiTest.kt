package org.ossiaustria.lib.domain.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
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
class GroupApiTest : AbstractApiTest() {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val idExisting = UUID.randomUUID()

    lateinit var subject: GroupApi

    override fun createApi(retrofit: Retrofit) {
        subject = retrofit.create(GroupApi::class.java)
    }

    @Test
    fun `GroupApi get should retrieve one item `() {
        val group = runBlocking {
            subject.get(idExisting)
        }
        assertNotNull(group)
        assertNotNull(group.name)
        assertNotNull(group.id)

        assertSame(1, group.members.size)

        group.members.forEach {
            assertNotNull(it.id)
            assertNotNull(it.name)
            assertNotNull(it.memberType)
            assertNotNull(it.groupId)
            assertNotNull(it.avatarUrl)
        }
    }

    @Test
    fun `GroupApi getAll should retrieve all items `() {
        val items = runBlocking {
            subject.getOwn()
        }

        assertNotNull(items)
        Assert.assertEquals(items.size, 2)
    }

    override fun setupMockingMap(): Map<String, MockResponse> = mapOf(
        "groups/$idExisting" to MockResponse(
            JsonMocker.group(
                id = idExisting,
                personsMock = listOf(JsonMocker.person(groupId = idExisting),)
            )
        ),
        "groups" to MockResponse(
            JsonMocker.createList(
                listOf(
                    JsonMocker.group(personsMock = listOf(JsonMocker.person())),
                    JsonMocker.group(personsMock = listOf(JsonMocker.person()))
                )
            )
        )
    )

}