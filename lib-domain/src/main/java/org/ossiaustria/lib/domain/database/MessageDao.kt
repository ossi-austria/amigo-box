package org.ossiaustria.lib.domain.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.ossiaustria.lib.domain.database.entities.MessageEntity
import java.util.*

@Dao
abstract class MessageDao : SendableDao<MessageEntity>() {

    @Query("DELETE FROM messages")
    abstract override suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM messages ORDER BY retrievedAt DESC")
    abstract override suspend fun findAll(): List<MessageEntity>

    @Transaction
    @Query("SELECT * FROM messages ORDER BY retrievedAt ASC")
    abstract override suspend fun findAllOldest(): List<MessageEntity>

    @Transaction
    @Query("SELECT * FROM messages where id = :id")
    abstract override suspend fun findById(id: UUID): MessageEntity

    @Transaction
    @Query("SELECT * FROM messages where senderId = :id")
    abstract override suspend fun findBySender(id: UUID): List<MessageEntity>

    @Transaction
    @Query("SELECT * FROM messages where receiverId = :id")
    abstract override suspend fun findByReceiver(id: UUID): List<MessageEntity>
}