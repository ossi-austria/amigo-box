package org.ossiaustria.lib.domain.example.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import org.ossiaustria.lib.domain.example.models.Author

@Dao
interface AuthorDao {
    @Insert
    suspend fun insertAll(vararg items: Author)

    @Delete
    suspend fun delete(item: Author)

    @Query("SELECT * FROM authors")
    suspend fun getAll(): List<Author>
}