package org.ossiaustria.lib.domain.daos

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.ossiaustria.lib.domain.models.Author

@Dao
interface AuthorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Author>)

    @Insert
    suspend fun insert(item: Author)

    @Update
    suspend fun update(item: Author)

    @Query("SELECT * FROM authors where id = :id LIMIT 1")
    fun find(id: Long): Flow<Author>

    @Query("SELECT * FROM authors")
    fun findAll(): Flow<List<Author>>

    @Delete
    suspend fun delete(item: Author)

    @Query("DELETE FROM authors WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM authors")
    suspend fun deleteAll()

}