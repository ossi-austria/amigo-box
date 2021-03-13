package org.ossiaustria.lib.domain.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.ossiaustria.lib.domain.database.entities.AlbumEntity
import org.ossiaustria.lib.domain.database.entities.AlbumEntityWithData
import java.util.*

@Dao
internal abstract class AlbumDao : AbstractEntityDao<AlbumEntity, AlbumEntityWithData>() {

    @Query("DELETE FROM albums")
    abstract override suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM albums ORDER BY name ASC")
    abstract override suspend fun findAll(): List<AlbumEntityWithData>

    @Transaction
    @Query("SELECT * FROM albums where albumId = :id")
    abstract override suspend fun findById(id: UUID): AlbumEntityWithData

}