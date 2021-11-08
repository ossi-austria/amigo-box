package org.ossiaustria.lib.domain.modules

import org.koin.dsl.module
import org.ossiaustria.lib.domain.services.AlbumShareService
import org.ossiaustria.lib.domain.services.AuthService
import org.ossiaustria.lib.domain.services.AuthServiceImpl
import org.ossiaustria.lib.domain.services.CallService
import org.ossiaustria.lib.domain.services.CallServiceImpl
import org.ossiaustria.lib.domain.services.IncomingEventCallbackService
import org.ossiaustria.lib.domain.services.IncomingEventCallbackServiceImpl
import org.ossiaustria.lib.domain.services.MessageService
import org.ossiaustria.lib.domain.services.MockAlbumShareServiceImpl
import org.ossiaustria.lib.domain.services.MockMessageServiceImpl
import org.ossiaustria.lib.domain.services.MockMultimediaServiceImpl
import org.ossiaustria.lib.domain.services.MultimediaService
import org.ossiaustria.lib.domain.services.NfcInfoService
import org.ossiaustria.lib.domain.services.NfcInfoServiceImpl
import org.ossiaustria.lib.domain.services.TimelineService
import org.ossiaustria.lib.domain.services.TimelineServiceImpl

val serviceModule = module {
    single<AuthService> { AuthServiceImpl(get(), get(), get(), get(), get()) }
    single<MessageService> { MockMessageServiceImpl(get(), get()) }
    single<AlbumShareService> { MockAlbumShareServiceImpl(get(), get()) }
    single<IncomingEventCallbackService> { IncomingEventCallbackServiceImpl(get(), get()) }
    single<CallService> { CallServiceImpl(get(), get(), get()) }
    single<MultimediaService> { MockMultimediaServiceImpl(get(), get()) }
    single<TimelineService> { TimelineServiceImpl(get(), get(), get(), get()) }
    single<NfcInfoService> { NfcInfoServiceImpl(get(), get()) }
}

