package org.ossiaustria.lib.domain.api

import org.ossiaustria.lib.domain.models.Post
import retrofit2.http.GET
import retrofit2.http.Path

interface PostApi : Api {

    @GET("posts")
    suspend fun getAll(): List<Post>

    @GET("posts/{id}")
    suspend fun get(@Path("id") id: Long): Post
}