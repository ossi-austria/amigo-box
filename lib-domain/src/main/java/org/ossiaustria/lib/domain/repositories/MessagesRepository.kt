package org.ossiaustria.lib.domain.repositories

import com.dropbox.android.external.store4.Store
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.ossiaustria.lib.commons.DispatcherProvider
import org.ossiaustria.lib.domain.api.MessageApi
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.database.MessageDao
import org.ossiaustria.lib.domain.database.entities.MessageEntity
import org.ossiaustria.lib.domain.database.entities.toMessage
import org.ossiaustria.lib.domain.database.entities.toMessageEntity
import org.ossiaustria.lib.domain.models.Message
import org.ossiaustria.lib.domain.modules.UserContext
import timber.log.Timber
import java.util.*

interface MessageRepository {

    fun getAllMessages(refresh: Boolean = false): Flow<Resource<List<Message>>>
    fun getReceived(refresh: Boolean = false): Flow<Resource<List<Message>>>

    @ExperimentalCoroutinesApi
    fun getMessage(id: UUID, refresh: Boolean = false): Flow<Resource<Message>>
}

class MessageRepositoryImpl(
    private val messageApi: MessageApi,
    private val messageDao: MessageDao,
    dispatcherProvider: DispatcherProvider,
    private val userContext: UserContext,
) : MessageRepository,
    SingleAndCollectionStore<MessageEntity, MessageEntity, Message>(
        messageDao,
        dispatcherProvider
    ) {

    @ExperimentalCoroutinesApi
    @FlowPreview
    private val receivedStore: Store<String, List<Message>> =
        buildCollectionStore(
            fetchApi = { messageApi.getOwn() },
            readDao = { messageDao.findByReceiver(userContext.personId()!!) },
            transform = { it.toMessage() })

    override suspend fun fetchOne(id: UUID): Message = messageApi.getOne(id)
    override suspend fun defaultFetchAll(): List<Message> = messageApi.getReceived()

    override suspend fun writeItem(item: Message) {
        try {
            messageDao.insert(item.toMessageEntity())
        } catch (e: Exception) {
            Timber.e(e, "Store4 cannot write item: $item")
        }
    }

    override fun readItem(id: UUID): Flow<Message> =
        withFlowItem(messageDao.findById(id)) {
            it.toMessage()
        }

    override fun defaultReadAll(): Flow<List<MessageEntity>> = messageDao.findAll()

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun getAllMessages(refresh: Boolean): Flow<Resource<List<Message>>> = flow {
        listTransform(
            defaultCollectionStore.stream(
                newRequest(key = "all", refresh = refresh)
            )
        )
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun getReceived(refresh: Boolean): Flow<Resource<List<Message>>> = flow {
        listTransform(receivedStore.stream(newRequest(key = "received", refresh = refresh)))
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun getMessage(id: UUID, refresh: Boolean): Flow<Resource<Message>> = flow {
        itemTransform(singleStore.stream(newRequest(key = id, refresh = refresh)))
    }

    override fun transform(item: MessageEntity): Message = item.toMessage()

}



