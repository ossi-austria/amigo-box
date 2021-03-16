package org.ossiaustria.lib.domain.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.ossiaustria.lib.domain.database.entities.AlbumEntity
import org.ossiaustria.lib.domain.database.entities.AlbumEntityWithData
import java.util.*

@Dao
abstract class AlbumDao : AbstractEntityDao<AlbumEntity, AlbumEntityWithData>() {

    @Query("DELETE FROM albums")
    abstract override suspend fun deleteAll()

    @Query("DELETE FROM albums where albumId = :id")
    abstract override suspend fun deleteById(id: UUID)

    @Transaction
    @Query("SELECT * FROM albums ORDER BY name ASC")
    abstract override fun findAll(): Flow<List<AlbumEntityWithData>>

    @Transaction
    @Query("SELECT * FROM albums where albumId = :id")
    abstract override fun findById(id: UUID): Flow<AlbumEntityWithData>

}