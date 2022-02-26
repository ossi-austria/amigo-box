package org.ossiaustria.lib.domain.modules

import okhttp3.OkHttpClient
import org.koin.dsl.module
import org.ossiaustria.lib.domain.api.AlbumApi
import org.ossiaustria.lib.domain.api.AlbumShareApi
import org.ossiaustria.lib.domain.api.CallApi
import org.ossiaustria.lib.domain.api.GroupApi
import org.ossiaustria.lib.domain.api.MessageApi
import org.ossiaustria.lib.domain.api.MockInterceptor
import org.ossiaustria.lib.domain.api.MultimediaApi
import org.ossiaustria.lib.domain.api.NfcInfoApi
import org.ossiaustria.lib.domain.api.NoopMockInterceptor
import org.ossiaustria.lib.domain.api.OkHttpBuilder
import org.ossiaustria.lib.domain.api.OkHttpBuilder.Companion.BASE_URL
import org.ossiaustria.lib.domain.api.PersonApi
import org.ossiaustria.lib.domain.auth.AuthApi
import org.ossiaustria.lib.domain.auth.AuthInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val apiModule = module {
    single { AuthInterceptor() }
    single { UserContext(get()) }
    single<OkHttpClient> {
        OkHttpBuilder().build(
            get(),
            get()
        )
    }
    single<MockInterceptor> { NoopMockInterceptor() }
    single<Retrofit> {
        Retrofit.Builder()
            .client(get())
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { get<Retrofit>().create(AuthApi::class.java) }
    single { get<Retrofit>().create(AlbumApi::class.java) }
    single { get<Retrofit>().create(AlbumShareApi::class.java) }
    single { get<Retrofit>().create(CallApi::class.java) }
    single { get<Retrofit>().create(GroupApi::class.java) }
    single { get<Retrofit>().create(MessageApi::class.java) }
    single { get<Retrofit>().create(MultimediaApi::class.java) }
    single { get<Retrofit>().create(NfcInfoApi::class.java) }
    single { get<Retrofit>().create(PersonApi::class.java) }

}

