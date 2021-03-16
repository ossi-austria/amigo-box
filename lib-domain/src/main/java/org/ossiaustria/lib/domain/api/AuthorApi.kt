package org.ossiaustria.lib.domain.api

import org.ossiaustria.lib.domain.models.Author
import retrofit2.http.GET
import retrofit2.http.Path

internal interface AuthorApi : Api {

    @GET("authors")
    suspend fun getAll(): List<Author>

    @GET("authors/{id}")
    suspend fun get(@Path("id") id: Long): Author
}