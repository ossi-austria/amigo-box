package org.ossiaustria.lib.domain.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.ossiaustria.lib.domain.database.entities.CallEntity
import java.util.*

@Dao
abstract class CallDao : SendableDao<CallEntity, CallEntity>() {

    @Query("DELETE FROM calls")
    abstract override suspend fun deleteAll()

    @Query("DELETE FROM calls where id = :id")
    abstract override suspend fun deleteById(id: UUID)

    @Transaction
    @Query("SELECT * FROM calls ORDER BY retrievedAt DESC")
    abstract override fun findAll(): Flow<List<CallEntity>>

    @Transaction
    @Query("SELECT * FROM calls ORDER BY retrievedAt ASC")
    abstract override fun findAllOldest(): Flow<List<CallEntity>>

    @Transaction
    @Query("SELECT * FROM calls where id = :id")
    abstract override fun findById(id: UUID): Flow<CallEntity>

    @Transaction
    @Query("SELECT * FROM calls where senderId = :id")
    abstract override fun findBySender(id: UUID): Flow<List<CallEntity>>

    @Transaction
    @Query("SELECT * FROM calls where receiverId = :id")
    abstract override fun findByReceiver(id: UUID): Flow<List<CallEntity>>
}