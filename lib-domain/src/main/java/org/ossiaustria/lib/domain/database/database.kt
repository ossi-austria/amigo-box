package org.ossiaustria.lib.domain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.ossiaustria.lib.domain.database.converters.*
import org.ossiaustria.lib.domain.database.entities.*
import org.ossiaustria.lib.domain.models.Author
import org.ossiaustria.lib.domain.models.Comment
import org.ossiaustria.lib.domain.models.Post


internal interface AppDatabase {
    fun authorDao(): AuthorDao
    fun postDao(): PostDao
    fun commentDao(): CommentDao
    fun groupDao(): GroupDao
    fun personDao(): PersonDao
    fun callDao(): CallDao
    fun messageDao(): MessageDao
    fun multimediaDao(): MultimediaDao

    fun albumDao(): AlbumDao

    fun albumShareDao(): AlbumShareDao
}

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
        AlbumShareEntity::class,
        AlbumEntity::class,
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
abstract class AppDatabaseImpl : RoomDatabase(), AppDatabase {
    abstract override fun authorDao(): AuthorDao
    abstract override fun postDao(): PostDao
    abstract override fun commentDao(): CommentDao
    abstract override fun groupDao(): GroupDao
    abstract override fun personDao(): PersonDao
    abstract override fun callDao(): CallDao
    abstract override fun messageDao(): MessageDao
    abstract override fun multimediaDao(): MultimediaDao

    abstract override fun albumDao(): AlbumDao

    abstract override fun albumShareDao(): AlbumShareDao
}