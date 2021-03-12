package org.ossiaustria.lib.domain.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.ossiaustria.lib.domain.database.entities.PersonEntity
import java.util.*

@Dao
internal abstract class PersonDao : AbstractEntityDao<PersonEntity>() {

    @Query("DELETE FROM calls")
    abstract override suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM persons ORDER BY name ASC")
    abstract override suspend fun findAll(): List<PersonEntity>

    @Transaction
    @Query("SELECT * FROM persons where personId = :id")
    abstract override suspend fun findById(id: UUID): PersonEntity

}