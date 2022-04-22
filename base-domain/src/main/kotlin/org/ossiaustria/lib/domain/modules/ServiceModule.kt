package org.ossiaustria.lib.domain.modules

import org.koin.dsl.module
import org.ossiaustria.lib.domain.services.AlbumShareService
import org.ossiaustria.lib.domain.services.AuthService
import org.ossiaustria.lib.domain.services.AuthServiceImpl
import org.ossiaustria.lib.domain.services.CallService
import org.ossiaustria.lib.domain.services.CallServiceImpl
import org.ossiaustria.lib.domain.services.MessageService
import org.ossiaustria.lib.domain.services.MessageServiceImpl
import org.ossiaustria.lib.domain.services.MockAlbumShareServiceImpl
import org.ossiaustria.lib.domain.services.NfcInfoService
import org.ossiaustria.lib.domain.services.NfcInfoServiceImpl
import org.ossiaustria.lib.domain.services.TimelineService
import org.ossiaustria.lib.domain.services.TimelineServiceImpl
import org.ossiaustria.lib.domain.services.events.IncomingEventCallbackService
import org.ossiaustria.lib.domain.services.events.IncomingEventCallbackServiceImpl

val serviceModule = module {
    single<AuthService> { AuthServiceImpl(get(), get(), get(), get()) }
    single<MessageService> { MessageServiceImpl(get(), get()) }
    single<AlbumShareService> { MockAlbumShareServiceImpl(get(), get()) }
    single<IncomingEventCallbackService> { IncomingEventCallbackServiceImpl(get(), get()) }
    single<CallService> { CallServiceImpl(get(), get()) }
    single<TimelineService> { TimelineServiceImpl(get(), get()) }
    single<NfcInfoService> { NfcInfoServiceImpl(get(), get()) }
}

