package org.ossiaustria.lib.domain.database

import androidx.room.*
import org.ossiaustria.lib.domain.models.Post
import org.ossiaustria.lib.domain.models.PostWithComments

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