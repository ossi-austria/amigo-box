package org.ossiaustria.lib.domain.repositories

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.api.MessageApi
import org.ossiaustria.lib.domain.database.AppDatabaseImpl
import org.ossiaustria.lib.domain.database.MessageDao
import org.ossiaustria.lib.domain.database.entities.MessageEntity
import org.ossiaustria.lib.domain.models.Message
import org.robolectric.RobolectricTestRunner
import java.util.*

@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
internal class MessagesRepositoryTest : AbstractRepositoryTest<MessageEntity, Message>() {

    lateinit var subject: MessageRepository

    lateinit var messageDao: MessageDao

    @RelaxedMockK
    lateinit var messageApi: MessageApi

    @Before
    fun before() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, AppDatabaseImpl::class.java).build()
        messageDao = db.messageDao()
        MockKAnnotations.init(this, relaxUnitFun = true)

        subject = MessageRepositoryImpl(messageApi, messageDao, testDispatcherProvider)
    }

    @InternalCoroutinesApi
    @Test
    fun `should use database and fetcher`() {

        runBlocking {
            val personId = UUID.randomUUID()

            val id1 = UUID.randomUUID()
            val id2 = UUID.randomUUID()

            val remoteList = listOf(
                Message(id1, senderId = personId, receiverId = personId, text = "text"),
                Message(id2, senderId = personId, receiverId = personId, text = "text"),
            )

            coEvery { messageApi.getAll() } answers { remoteList }

            val daoList = listOf(
                MessageEntity(
                    id1,
                    senderId = personId,
                    receiverId = personId,
                    text = "text"
                ),
            )

            messageDao.insertAll(daoList)

            testAllStates(daoList, remoteList, subject.getAllMessages())
        }
    }

}