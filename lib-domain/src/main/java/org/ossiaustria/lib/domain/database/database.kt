package org.ossiaustria.lib.domain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.ossiaustria.lib.domain.database.converters.CallTypeConverter
import org.ossiaustria.lib.domain.database.converters.MembershipTypeConverter
import org.ossiaustria.lib.domain.database.converters.MultimediaTypeConverter
import org.ossiaustria.lib.domain.database.converters.NFCTagTypeConverter
import org.ossiaustria.lib.domain.database.converters.UUIDConverter
import org.ossiaustria.lib.domain.database.entities.AlbumEntity
import org.ossiaustria.lib.domain.database.entities.AlbumShareEntity
import org.ossiaustria.lib.domain.database.entities.CallEntity
import org.ossiaustria.lib.domain.database.entities.GroupEntity
import org.ossiaustria.lib.domain.database.entities.MessageEntity
import org.ossiaustria.lib.domain.database.entities.MultimediaEntity
import org.ossiaustria.lib.domain.database.entities.NfcTagEntity
import org.ossiaustria.lib.domain.database.entities.PersonEntity

internal interface AppDatabase {
    fun groupDao(): GroupDao
    fun personDao(): PersonDao
    fun callDao(): CallDao
    fun messageDao(): MessageDao
    fun multimediaDao(): MultimediaDao
    fun albumDao(): AlbumDao
    fun albumShareDao(): AlbumShareDao
    fun nfcTagDao(): NfcTagDao
}

@Database(
    entities = [
        GroupEntity::class,
        PersonEntity::class,
        CallEntity::class,
        MessageEntity::class,
        MultimediaEntity::class,
        AlbumShareEntity::class,
        AlbumEntity::class,
        NfcTagEntity::class,
    ],
    version = 3,
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
internal abstract class AppDatabaseImpl : RoomDatabase(), AppDatabase {
    abstract override fun groupDao(): GroupDao
    abstract override fun personDao(): PersonDao
    abstract override fun callDao(): CallDao
    abstract override fun messageDao(): MessageDao
    abstract override fun multimediaDao(): MultimediaDao

    abstract override fun albumDao(): AlbumDao

    abstract override fun albumShareDao(): AlbumShareDao
    abstract override fun nfcTagDao(): NfcTagDao
}