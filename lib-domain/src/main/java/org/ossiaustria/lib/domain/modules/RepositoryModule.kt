package org.ossiaustria.lib.domain.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.ossiaustria.lib.domain.api.AuthorApi
import org.ossiaustria.lib.domain.daos.AuthorDao
import org.ossiaustria.lib.domain.repositories.AuthorRepository
import org.ossiaustria.lib.domain.repositories.AuthorRepositoryImpl
import org.ossiaustria.lib.domain.repositories.DefaultDispatcherProvider
import org.ossiaustria.lib.domain.repositories.DispatcherProvider

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    @ActivityRetainedScoped
    fun dispatcherProvider(): DispatcherProvider {
        return DefaultDispatcherProvider()
    }

    @Provides
    @ActivityRetainedScoped
    fun authorRepository(
        authorApi: AuthorApi,
        authorDao: AuthorDao,
        dispatcherProvider: DispatcherProvider
    ): AuthorRepository {
        return AuthorRepositoryImpl(authorApi, authorDao, dispatcherProvider)
    }
}