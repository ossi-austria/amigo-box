package org.ossiaustria.lib.domain.repositories

import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
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

    fun getAllMessages(): Flow<Resource<List<Message>>>
    fun getReceived(): Flow<Resource<List<Message>>>

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    fun getMessage(id: UUID): Flow<Resource<Message>>
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
            fetchApi = { messageApi.getAllReceived() },
            readDao = { messageDao.findByReceiver(userContext.personId()) },
            transform = { it.toMessage() })

    override suspend fun fetchOne(id: UUID): Message = messageApi.get(id)
    override suspend fun defaultFetchAll(): List<Message> = messageApi.getAllReceived()

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
    @InternalCoroutinesApi
    override fun getAllMessages(): Flow<Resource<List<Message>>> = flow {
        listTransform(
            defaultCollectionStore.stream(
                StoreRequest.cached(key = "all", refresh = true)
            )
        )
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getReceived(): Flow<Resource<List<Message>>> = flow {
        listTransform(receivedStore.stream(StoreRequest.cached(key = "received", refresh = true)))
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getMessage(id: UUID): Flow<Resource<Message>> = flow {
        itemTransform(singleStore.stream(StoreRequest.cached(key = id, refresh = true)))
    }

    override fun transform(item: MessageEntity): Message = item.toMessage()

}



