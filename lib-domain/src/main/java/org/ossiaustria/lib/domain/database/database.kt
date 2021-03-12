package org.ossiaustria.lib.domain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.ossiaustria.lib.domain.database.converters.*
import org.ossiaustria.lib.domain.database.entities.*
import org.ossiaustria.lib.domain.models.Author
import org.ossiaustria.lib.domain.models.Comment
import org.ossiaustria.lib.domain.models.Post

@Database(
    entities = [
        Author::class,
        Post::class,
        Comment::class,
        GroupEntity::class,
        PersonEntity::class,
        CallEntity::class,
        MessageEntity::class,
        MultimediaEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    value = [
        UUIDConverter::class,
        // EnumConverters
        CallTypeConverter::class,
        NFCTagTypeConverter::class,
        MembershipTypeConverter::class,
        MultimediaTypeConverter::class,
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authorDao(): AuthorDao
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
    internal abstract fun groupDao(): GroupDao
    internal abstract fun personDao(): PersonDao
    internal abstract fun callDao(): CallDao
    internal abstract fun messageDao(): MessageDao
    internal abstract fun multimediaDao(): MultimediaDao
}