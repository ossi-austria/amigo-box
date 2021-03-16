package org.ossiaustria.lib.domain.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.ossiaustria.lib.commons.DispatcherProvider
import org.ossiaustria.lib.domain.api.AlbumApi
import org.ossiaustria.lib.domain.api.AuthorApi
import org.ossiaustria.lib.domain.database.AlbumDao
import org.ossiaustria.lib.domain.database.AuthorDao
import org.ossiaustria.lib.domain.database.MultimediaDao
import org.ossiaustria.lib.domain.repositories.AlbumRepository
import org.ossiaustria.lib.domain.repositories.AlbumRepositoryImpl
import org.ossiaustria.lib.domain.repositories.AuthorRepository
import org.ossiaustria.lib.domain.repositories.AuthorRepositoryImpl

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    @ActivityRetainedScoped
    internal fun authorRepository(
        authorApi: AuthorApi,
        authorDao: AuthorDao,
        dispatcherProvider: DispatcherProvider
    ): AuthorRepository {
        return AuthorRepositoryImpl(authorApi, authorDao, dispatcherProvider)
    }

    @PublishedApi
    @Provides
    @ActivityRetainedScoped
    internal fun albumRepository(
        albumApi: AlbumApi,
        albumDao: AlbumDao,
        multimediaDao: MultimediaDao,
        dispatcherProvider: DispatcherProvider
    ): AlbumRepository {
        return AlbumRepositoryImpl(albumApi, albumDao, multimediaDao, dispatcherProvider)
    }
}