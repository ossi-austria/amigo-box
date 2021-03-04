package org.ossiaustria.lib.domain.daos

import androidx.room.Database
import androidx.room.RoomDatabase
import org.ossiaustria.lib.domain.models.Author
import org.ossiaustria.lib.domain.models.Comment
import org.ossiaustria.lib.domain.models.Post

@Database(
    entities = [Author::class, Post::class, Comment::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authorDao(): AuthorDao
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
}