package org.ossiaustria.lib.domain.example.daos

import androidx.room.*
import org.ossiaustria.lib.domain.example.models.Post
import org.ossiaustria.lib.domain.example.models.PostWithComments

@Dao
interface PostDao {
    @Insert
    suspend fun insertAll(vararg items: Post)

    @Delete
    suspend fun delete(item: Post)

    @Query("SELECT * FROM posts")
    suspend fun getAll(): List<Post>

    @Transaction
    @Query("SELECT * FROM posts")
    suspend fun getPostsWithComments(): List<PostWithComments>
}