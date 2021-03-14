package org.ossiaustria.lib.domain.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.ossiaustria.lib.domain.database.*
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @PublishedApi
    @Provides
    @Singleton
    internal fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabaseImpl::class.java, "AmigoBoxDatabase")
            .build()
    }

    @PublishedApi
    @Provides
    internal
    fun provideAuthorDao(appDatabase: AppDatabase): AuthorDao {
        return appDatabase.authorDao()
    }

    @PublishedApi
    @Provides
    internal fun albumDao(appDatabase: AppDatabase): AlbumDao {
        return appDatabase.albumDao()
    }

    @PublishedApi
    @Provides
    internal fun multimediaDao(appDatabase: AppDatabase): MultimediaDao {
        return appDatabase.multimediaDao()
    }
}