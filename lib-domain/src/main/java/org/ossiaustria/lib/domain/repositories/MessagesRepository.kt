package org.ossiaustria.lib.domain.repositories

import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.ossiaustria.lib.commons.DispatcherProvider
import org.ossiaustria.lib.domain.api.MessageApi
import org.ossiaustria.lib.domain.common.Effect
import org.ossiaustria.lib.domain.database.MessageDao
import org.ossiaustria.lib.domain.database.entities.MessageEntity
import org.ossiaustria.lib.domain.database.entities.toMessage
import org.ossiaustria.lib.domain.database.entities.toMessageEntity
import org.ossiaustria.lib.domain.models.Message
import timber.log.Timber
import java.util.*


interface MessageRepository {

    fun getAllMessages(): Flow<Effect<List<Message>>>

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    fun getMessage(id: UUID): Flow<Effect<Message>>
}

internal class MessageRepositoryImpl(
    private val messageApi: MessageApi,
    private val messageDao: MessageDao,
    private val dispatcherProvider: DispatcherProvider
) : MessageRepository,
    SingleAndCollectionStore<MessageEntity, MessageEntity, Message>(messageDao) {

    override suspend fun fetchOne(id: UUID): Message = messageApi.get(id)
    override suspend fun fetchAll(): List<Message> = messageApi.getAll()

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


    override fun readAllItems(): Flow<List<Message>> =
        withFlowCollection(messageDao.findAll()) {
            it.toMessage()
        }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getAllMessages(): Flow<Effect<List<Message>>> = flow {
        collectionStore.stream(StoreRequest.cached(key = "all", refresh = true))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<List<Message>> ->
                transformResponseToOutcome(response, onNewData = { Effect.loading() })
            }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getMessage(id: UUID): Flow<Effect<Message>> = flow {
        singleStore.stream(StoreRequest.cached(key = id, refresh = true))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<Message> ->
                transformResponseToOutcome(response, onNewData = { Effect.loading() })
            }
    }
}



