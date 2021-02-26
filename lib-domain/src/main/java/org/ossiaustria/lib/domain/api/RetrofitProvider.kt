package org.ossiaustria.lib.domain.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface Api

object RetrofitProvider {

    fun build(rootUrl: String) = Retrofit.Builder()
        .baseUrl(rootUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    inline fun <reified T> create(rootUrl: String) = build(rootUrl).create(T::class.java)
}