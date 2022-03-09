package org.ossiaustria.lib.domain.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.ossiaustria.lib.domain.database.entities.PersonEntity
import java.util.*

@Dao
abstract class PersonDao : AbstractEntityDao<PersonEntity, PersonEntity>() {

    @Query("DELETE FROM persons")
    abstract override suspend fun deleteAll()

    @Query("DELETE FROM persons where personId = :id")
    abstract override suspend fun deleteById(id: UUID)

    @Transaction
    @Query("SELECT * FROM persons ORDER BY name ASC")
    abstract override fun findAll(): Flow<List<PersonEntity>>

    @Transaction
    @Query("SELECT * FROM persons where personId = :id")
    abstract override fun findById(id: UUID): Flow<PersonEntity>

}