package org.ossiaustria.lib.domain.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.ossiaustria.lib.domain.database.AlbumDao
import org.ossiaustria.lib.domain.database.AlbumShareDao
import org.ossiaustria.lib.domain.database.AppDatabase
import org.ossiaustria.lib.domain.database.AppDatabaseImpl
import org.ossiaustria.lib.domain.database.CallDao
import org.ossiaustria.lib.domain.database.GroupDao
import org.ossiaustria.lib.domain.database.MessageDao
import org.ossiaustria.lib.domain.database.MultimediaDao
import org.ossiaustria.lib.domain.database.PersonDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @PublishedApi
    @Provides
    @Singleton
    internal fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room
            .databaseBuilder(appContext, AppDatabaseImpl::class.java, "AmigoBoxDatabase")
            .fallbackToDestructiveMigration()
            .build()
    }

    @PublishedApi
    @Provides
    internal fun groupDao(appDatabase: AppDatabase): GroupDao {
        return appDatabase.groupDao()
    }

    @Provides
    internal fun albumDao(appDatabase: AppDatabase): AlbumDao {
        return appDatabase.albumDao()
    }

    @Provides
    internal fun albumShareDao(appDatabase: AppDatabase): AlbumShareDao {
        return appDatabase.albumShareDao()
    }

    @Provides
    internal fun callDao(appDatabase: AppDatabase): CallDao {
        return appDatabase.callDao()
    }

    @PublishedApi
    @Provides
    internal fun multimediaDao(appDatabase: AppDatabase): MultimediaDao {
        return appDatabase.multimediaDao()
    }

    @PublishedApi
    @Provides
    internal fun messageDao(appDatabase: AppDatabase): MessageDao {
        return appDatabase.messageDao()
    }

    @PublishedApi
    @Provides
    internal fun personDao(appDatabase: AppDatabase): PersonDao {
        return appDatabase.personDao()
    }
}