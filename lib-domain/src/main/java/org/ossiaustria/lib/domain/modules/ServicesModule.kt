package org.ossiaustria.lib.domain.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import org.ossiaustria.lib.domain.auth.AuthApi
import org.ossiaustria.lib.domain.repositories.AlbumShareRepository
import org.ossiaustria.lib.domain.repositories.CallRepository
import org.ossiaustria.lib.domain.repositories.MessageRepository
import org.ossiaustria.lib.domain.repositories.MultimediaRepository
import org.ossiaustria.lib.domain.repositories.SettingsRepository
import org.ossiaustria.lib.domain.services.AlbumShareService
import org.ossiaustria.lib.domain.services.AuthService
import org.ossiaustria.lib.domain.services.AuthServiceImpl
import org.ossiaustria.lib.domain.services.CallService
import org.ossiaustria.lib.domain.services.MessageService
import org.ossiaustria.lib.domain.services.MockAlbumShareServiceImpl
import org.ossiaustria.lib.domain.services.MockCallServiceImpl
import org.ossiaustria.lib.domain.services.MockMessageServiceImpl
import org.ossiaustria.lib.domain.services.MockMultimediaServiceImpl
import org.ossiaustria.lib.domain.services.MultimediaService
import org.ossiaustria.lib.domain.services.TimelineService
import org.ossiaustria.lib.domain.services.TimelineServiceImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ServicesModule {

    @Provides
    @Singleton
    fun authService(
        ioDispatcher: CoroutineDispatcher,
        authApi: AuthApi,
        settingsRepository: SettingsRepository
    ): AuthService {
        return AuthServiceImpl(ioDispatcher, authApi, settingsRepository)
    }

    @Provides
    @Singleton
    fun messageService(
        ioDispatcher: CoroutineDispatcher,
        messageRepository: MessageRepository,
    ): MessageService {
        return MockMessageServiceImpl(ioDispatcher, messageRepository)
    }

    @Provides
    @Singleton
    fun albumShareService(
        ioDispatcher: CoroutineDispatcher,
        albumShareRepository: AlbumShareRepository,
    ): AlbumShareService {
        return MockAlbumShareServiceImpl(ioDispatcher, albumShareRepository)
    }

    @Provides
    @Singleton
    fun callService(
        ioDispatcher: CoroutineDispatcher,
        callRepository: CallRepository,
    ): CallService {
        return MockCallServiceImpl(ioDispatcher, callRepository)
    }

    @Provides
    @Singleton
    fun multimediaService(
        ioDispatcher: CoroutineDispatcher,
        multimediaRepository: MultimediaRepository,
    ): MultimediaService {
        return MockMultimediaServiceImpl(ioDispatcher, multimediaRepository)
    }

    @Provides
    @Singleton
    fun timelineService(
        albumShareService: AlbumShareService,
        callService: CallService,
        messageService: MessageService,
        multimediaService: MultimediaService,
    ): TimelineService {
        return TimelineServiceImpl(
            albumShareService,
            callService,
            messageService,
            multimediaService
        )
    }
}