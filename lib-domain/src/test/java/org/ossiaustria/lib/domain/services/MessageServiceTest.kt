package org.ossiaustria.lib.domain.services

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.ossiaustria.lib.commons.testing.TestCoroutineRule
import org.ossiaustria.lib.domain.repositories.MessageRepository

@ExperimentalCoroutinesApi
class MessageServiceTest {

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @RelaxedMockK
    lateinit var messageRepository: MessageRepository

    lateinit var subject: MessageService

    @Before
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        subject = MockMessageServiceImpl(coroutineRule.dispatcher, messageRepository)
    }

    @Test
    fun `login should return Account when credentials are valid`() {

    }

}