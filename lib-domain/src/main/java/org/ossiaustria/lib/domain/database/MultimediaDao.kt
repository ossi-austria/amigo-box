package org.ossiaustria.lib.domain.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.ossiaustria.lib.domain.database.entities.MultimediaEntity
import java.util.*

@Dao
abstract class MultimediaDao : SendableDao<MultimediaEntity, MultimediaEntity>() {

    @Query("DELETE FROM multimedias")
    abstract override suspend fun deleteAll()

    @Query("DELETE FROM multimedias where id = :id")
    abstract override suspend fun deleteById(id: UUID)

    @Transaction
    @Query("SELECT * FROM multimedias ORDER BY retrievedAt DESC")
    abstract override fun findAll(): Flow<List<MultimediaEntity>>

    @Transaction
    @Query("SELECT * FROM multimedias ORDER BY retrievedAt ASC")
    abstract override fun findAllOldest(): Flow<List<MultimediaEntity>>

    @Transaction
    @Query("SELECT * FROM multimedias where id = :id")
    abstract override fun findById(id: UUID): Flow<MultimediaEntity>

    @Transaction
    @Query("SELECT * FROM multimedias where senderId = :id")
    abstract override fun findBySender(id: UUID): Flow<List<MultimediaEntity>>

    @Transaction
    @Query("SELECT * FROM multimedias where receiverId = :id")
    abstract override fun findByReceiver(id: UUID): Flow<List<MultimediaEntity>>
}