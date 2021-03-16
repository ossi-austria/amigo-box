package org.ossiaustria.lib.domain.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.ossiaustria.lib.domain.database.entities.AlbumShareEntity
import org.ossiaustria.lib.domain.database.entities.AlbumShareEntityWithData
import java.util.*

@Dao
abstract class AlbumShareDao : SendableDao<AlbumShareEntity>() {

    @Query("DELETE FROM album_shares")
    abstract override suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM album_shares ORDER BY retrievedAt DESC")
    abstract override suspend fun findAll(): List<AlbumShareEntity>

    @Transaction
    @Query("SELECT * FROM album_shares ORDER BY retrievedAt DESC")
    abstract suspend fun findAllWithData(): List<AlbumShareEntityWithData>

    @Transaction
    @Query("SELECT * FROM album_shares ORDER BY retrievedAt ASC")
    abstract override suspend fun findAllOldest(): List<AlbumShareEntity>

    @Transaction
    @Query("SELECT * FROM album_shares where id = :id")
    abstract override suspend fun findById(id: UUID): AlbumShareEntity

    @Transaction
    @Query("SELECT * FROM album_shares where senderId = :id")
    abstract override suspend fun findBySender(id: UUID): List<AlbumShareEntity>

    @Transaction
    @Query("SELECT * FROM album_shares where receiverId = :id")
    abstract override suspend fun findByReceiver(id: UUID): List<AlbumShareEntity>
}