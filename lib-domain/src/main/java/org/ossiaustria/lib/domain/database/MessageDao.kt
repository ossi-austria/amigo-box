package org.ossiaustria.lib.domain.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.ossiaustria.lib.domain.database.entities.MessageEntity
import java.util.*

@Dao
abstract class MessageDao : SendableDao<MessageEntity, MessageEntity>() {

    @Query("DELETE FROM messages")
    abstract override suspend fun deleteAll()

    @Query("DELETE FROM messages where id = :id")
    abstract override suspend fun deleteById(id: UUID)

    @Transaction
    @Query("SELECT * FROM messages ORDER BY retrievedAt DESC")
    abstract override fun findAll(): Flow<List<MessageEntity>>

    @Transaction
    @Query("SELECT * FROM messages ORDER BY retrievedAt ASC")
    abstract override fun findAllOldest(): Flow<List<MessageEntity>>

    @Transaction
    @Query("SELECT * FROM messages where id = :id")
    abstract override fun findById(id: UUID): Flow<MessageEntity>

    @Transaction
    @Query("SELECT * FROM messages where senderId = :id")
    abstract override fun findBySender(id: UUID): Flow<List<MessageEntity>>

    @Transaction
    @Query("SELECT * FROM messages where receiverId = :id")
    abstract override fun findByReceiver(id: UUID): Flow<List<MessageEntity>>

    @Transaction
    @Query("SELECT * FROM messages where receiverId = :id or senderId = :id ")
    abstract override fun findByPerson(id: UUID): Flow<List<MessageEntity>>
}