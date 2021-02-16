package org.ossiaustria.lib.domain.example.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import org.ossiaustria.lib.domain.example.models.Comment

@Dao
interface CommentDao {
    @Insert
    suspend fun insertAll(vararg items: Comment)

    @Delete
    suspend fun delete(item: Comment)

    @Query("SELECT * FROM comments")
    suspend fun getAll(): List<Comment>
}