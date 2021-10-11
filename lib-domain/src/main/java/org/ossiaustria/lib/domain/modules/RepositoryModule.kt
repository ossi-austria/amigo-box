package org.ossiaustria.lib.domain.modules

import org.koin.dsl.module
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
import org.ossiaustria.lib.domain.repositories.NfcTagRepository
import org.ossiaustria.lib.domain.repositories.NfcTagRepositoryImpl
import org.ossiaustria.lib.domain.repositories.PersonRepository
import org.ossiaustria.lib.domain.repositories.PersonRepositoryImpl

val repositoryModule = module {
    single<AlbumRepository> { AlbumRepositoryImpl(get(), get(), get(), get()) }
    single<AlbumShareRepository> { AlbumShareRepositoryImpl(get(), get(), get()) }
    single<CallRepository> { CallRepositoryImpl(get(), get(), get()) }
    single<GroupRepository> { GroupRepositoryImpl(get(), get(), get(), get()) }
    single<MessageRepository> { MessageRepositoryImpl(get(), get(), get(), get()) }
    single<MultimediaRepository> { MultimediaRepositoryImpl(get(), get(), get()) }
    single<NfcTagRepository> { NfcTagRepositoryImpl(get(), get(), get()) }
    single<PersonRepository> { PersonRepositoryImpl(get(), get(), get()) }
}
