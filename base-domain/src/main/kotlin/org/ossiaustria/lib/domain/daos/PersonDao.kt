package org.ossiaustria.lib.domain.daos

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.ossiaustria.lib.domain.models.Person

@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Person>)

    @Insert
    suspend fun insert(item: Person)

    @Update
    suspend fun update(item: Person)

    @Query("Select * from persons")
    fun findAll(): Flow<List<Person>>

    @Query("Select * from persons where id = :key")
    fun find(key: Long): Flow<Person>

    @Query("DELETE FROM persons WHERE id = :key")
    suspend fun delete(key: Long)

    @Query("DELETE from persons")
    suspend fun deleteAll()

}