package org.ossiaustria.lib.domain.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.ossiaustria.lib.domain.database.entities.CallEntity
import java.util.*

@Dao
abstract class CallDao : SendableDao<CallEntity>() {

    @Query("DELETE FROM calls")
    abstract override suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM calls ORDER BY retrievedAt DESC")
    abstract override suspend fun findAll(): List<CallEntity>

    @Transaction
    @Query("SELECT * FROM calls ORDER BY retrievedAt ASC")
    abstract override suspend fun findAllOldest(): List<CallEntity>

    @Transaction
    @Query("SELECT * FROM calls where id = :id")
    abstract override suspend fun findById(id: UUID): CallEntity

    @Transaction
    @Query("SELECT * FROM calls where senderId = :id")
    abstract override suspend fun findBySender(id: UUID): List<CallEntity>

    @Transaction
    @Query("SELECT * FROM calls where receiverId = :id")
    abstract override suspend fun findByReceiver(id: UUID): List<CallEntity>
}