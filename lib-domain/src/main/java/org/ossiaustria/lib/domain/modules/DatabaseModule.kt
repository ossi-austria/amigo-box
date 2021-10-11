package org.ossiaustria.lib.domain.modules

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.ossiaustria.lib.domain.database.AppDatabase
import org.ossiaustria.lib.domain.database.AppDatabaseImpl

val databaseModule = module {
    single<AppDatabase> {
        Room
            .databaseBuilder(androidContext(), AppDatabaseImpl::class.java, "AmigoBoxDatabase")
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().albumDao() }
    single { get<AppDatabase>().albumShareDao() }
    single { get<AppDatabase>().callDao() }
    single { get<AppDatabase>().groupDao() }
    single { get<AppDatabase>().messageDao() }
    single { get<AppDatabase>().multimediaDao() }
    single { get<AppDatabase>().nfcTagDao() }
    single { get<AppDatabase>().personDao() }
}

