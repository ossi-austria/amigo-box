package org.ossiaustria.lib.domain.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.ossiaustria.lib.commons.DispatcherProvider
import org.ossiaustria.lib.domain.api.AlbumApi
import org.ossiaustria.lib.domain.api.AlbumShareApi
import org.ossiaustria.lib.domain.api.CallApi
import org.ossiaustria.lib.domain.api.GroupApi
import org.ossiaustria.lib.domain.api.MessageApi
import org.ossiaustria.lib.domain.api.MultimediaApi
import org.ossiaustria.lib.domain.api.PersonApi
import org.ossiaustria.lib.domain.database.AlbumDao
import org.ossiaustria.lib.domain.database.AlbumShareDao
import org.ossiaustria.lib.domain.database.CallDao
import org.ossiaustria.lib.domain.database.GroupDao
import org.ossiaustria.lib.domain.database.MessageDao
import org.ossiaustria.lib.domain.database.MultimediaDao
import org.ossiaustria.lib.domain.database.PersonDao
import org.ossiaustria.lib.domain.repositories.AlbumRepository
import org.ossiaustria.lib.domain.repositories.AlbumRepositoryImpl
import org.ossiaustria.lib.domain.repositories.AlbumShareRepository
import org.ossiaustria.lib.domain.repositories.AlbumShareRepositoryImpl
import org.ossiaustria.lib.domain.repositories.CallRepository
import org.ossiaustria.lib.domain.repositories.CallRepositoryImpl
import org.ossiaustria.lib.domain.repositories.GroupRepository
import org.ossiaustria.lib.domain.repositories.GroupRepositoryImpl
import org.ossiaustria.lib.domain.repositories.MessageRepository
import org.ossiaustria.lib.domain.repositories.MessageRepositoryImpl
import org.ossiaustria.lib.domain.repositories.MultimediaRepository
import org.ossiaustria.lib.domain.repositories.MultimediaRepositoryImpl
import org.ossiaustria.lib.domain.repositories.PersonRepository
import org.ossiaustria.lib.domain.repositories.PersonRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @PublishedApi
    @Provides
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
    internal fun groupRepository(
        groupApi: GroupApi,
        groupDao: GroupDao,
        personDao: PersonDao,
        dispatcherProvider: DispatcherProvider
    ): GroupRepository {
        return GroupRepositoryImpl(groupApi, groupDao, personDao, dispatcherProvider)
    }

    @PublishedApi
    @Provides
    internal fun personRepository(
        personApi: PersonApi,
        personDao: PersonDao,
        dispatcherProvider: DispatcherProvider
    ): PersonRepository {
        return PersonRepositoryImpl(personApi, personDao, dispatcherProvider)
    }

    @PublishedApi
    @Provides
    internal fun messageRepository(
        messageApi: MessageApi,
        messageDao: MessageDao,
        dispatcherProvider: DispatcherProvider,
        userContext: UserContext
    ): MessageRepository {
        return MessageRepositoryImpl(messageApi, messageDao, dispatcherProvider, userContext)
    }

    @PublishedApi
    @Provides
    internal fun callRepository(
        callApi: CallApi,
        callDao: CallDao,
        dispatcherProvider: DispatcherProvider,
    ): CallRepository {
        return CallRepositoryImpl(callApi, callDao, dispatcherProvider)
    }

    @PublishedApi
    @Provides
    internal fun multimediaRepository(
        multimediaApi: MultimediaApi,
        multimediaDao: MultimediaDao,
        dispatcherProvider: DispatcherProvider,
    ): MultimediaRepository {
        return MultimediaRepositoryImpl(multimediaApi, multimediaDao, dispatcherProvider)
    }

    @PublishedApi
    @Provides
    internal fun albumShareRepository(
        albumShareApi: AlbumShareApi,
        albumShareDao: AlbumShareDao,
        dispatcherProvider: DispatcherProvider,
    ): AlbumShareRepository {
        return AlbumShareRepositoryImpl(albumShareApi, albumShareDao, dispatcherProvider)
    }
}