package org.ossiaustria.lib.domain.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.ossiaustria.lib.domain.database.entities.MultimediaEntity
import java.util.*

@Dao
internal abstract class MultimediaDao : SendableDao<MultimediaEntity>() {

    @Query("DELETE FROM multimedias")
    abstract override suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM multimedias ORDER BY retrievedAt DESC")
    abstract override suspend fun findAll(): List<MultimediaEntity>

    @Transaction
    @Query("SELECT * FROM multimedias ORDER BY retrievedAt ASC")
    abstract override suspend fun findAllOldest(): List<MultimediaEntity>

    @Transaction
    @Query("SELECT * FROM multimedias where id = :id")
    abstract override suspend fun findById(id: UUID): MultimediaEntity

    @Transaction
    @Query("SELECT * FROM multimedias where senderId = :id")
    abstract override suspend fun findBySender(id: UUID): List<MultimediaEntity>

    @Transaction
    @Query("SELECT * FROM multimedias where receiverId = :id")
    abstract override suspend fun findByReceiver(id: UUID): List<MultimediaEntity>
}