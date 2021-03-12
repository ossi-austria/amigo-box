package org.ossiaustria.lib.domain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.ossiaustria.lib.domain.database.converters.MembershipTypeConverter
import org.ossiaustria.lib.domain.database.converters.UUIDConverter
import org.ossiaustria.lib.domain.database.entities.GroupEntity
import org.ossiaustria.lib.domain.database.entities.MemberEntity
import org.ossiaustria.lib.domain.models.Author
import org.ossiaustria.lib.domain.models.Comment
import org.ossiaustria.lib.domain.models.Post

@Database(
    entities = [
        Author::class,
        Post::class,
        Comment::class,
        GroupEntity::class,
        MemberEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    value = [
        UUIDConverter::class,
        MembershipTypeConverter::class,
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authorDao(): AuthorDao
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
    internal abstract fun groupDao(): GroupDao
    internal abstract fun personDao(): PersonDao
}