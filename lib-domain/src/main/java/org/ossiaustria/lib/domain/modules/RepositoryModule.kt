package org.ossiaustria.lib.domain.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.ossiaustria.lib.commons.DispatcherProvider
import org.ossiaustria.lib.domain.api.AlbumApi
import org.ossiaustria.lib.domain.api.GroupApi
import org.ossiaustria.lib.domain.database.AlbumDao
import org.ossiaustria.lib.domain.database.GroupDao
import org.ossiaustria.lib.domain.database.MultimediaDao
import org.ossiaustria.lib.domain.database.PersonDao
import org.ossiaustria.lib.domain.repositories.AlbumRepository
import org.ossiaustria.lib.domain.repositories.AlbumRepositoryImpl
import org.ossiaustria.lib.domain.repositories.GroupRepository
import org.ossiaustria.lib.domain.repositories.GroupRepositoryImpl

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

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

    @PublishedApi
    @Provides
    @ActivityRetainedScoped
    internal fun groupRepository(
        groupApi: GroupApi,
        groupDao: GroupDao,
        personDao: PersonDao,
        dispatcherProvider: DispatcherProvider
    ): GroupRepository {
        return GroupRepositoryImpl(groupApi, groupDao, personDao, dispatcherProvider)
    }
}