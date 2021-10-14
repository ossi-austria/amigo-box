package org.ossiaustria.lib.domain.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.ossiaustria.lib.domain.database.entities.AlbumShareEntity
import org.ossiaustria.lib.domain.database.entities.AlbumShareEntityWithData
import java.util.*

@Dao
abstract class AlbumShareDao : SendableDao<AlbumShareEntity, AlbumShareEntityWithData>() {

    @Query("DELETE FROM album_shares")
    abstract override suspend fun deleteAll()

    @Query("DELETE FROM album_shares where id = :id")
    abstract override suspend fun deleteById(id: UUID)

    @Transaction
    @Query("SELECT * FROM album_shares ORDER BY retrievedAt ASC")
    abstract override fun findAll(): Flow<List<AlbumShareEntityWithData>>

    @Transaction
    @Query("SELECT * FROM album_shares ORDER BY retrievedAt ASC")
    abstract override fun findAllOldest(): Flow<List<AlbumShareEntityWithData>>

    @Transaction
    @Query("SELECT * FROM album_shares where id = :id")
    abstract override fun findById(id: UUID): Flow<AlbumShareEntityWithData>

    @Transaction
    @Query("SELECT * FROM album_shares where senderId = :id")
    abstract override fun findBySender(id: UUID): Flow<List<AlbumShareEntityWithData>>

    @Transaction
    @Query("SELECT * FROM album_shares where receiverId = :id")
    abstract override fun findByReceiver(id: UUID): Flow<List<AlbumShareEntityWithData>>

    @Transaction
    @Query("SELECT * FROM album_shares where receiverId = :id or senderId = :id ")
    abstract override fun findByPerson(id: UUID): Flow<List<AlbumShareEntityWithData>>
}