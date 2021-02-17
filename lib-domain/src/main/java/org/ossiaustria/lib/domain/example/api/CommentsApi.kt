package org.ossiaustria.lib.domain.example.api

import org.ossiaustria.lib.domain.example.models.Comment
import retrofit2.http.GET

interface CommentsApi : Api {

    @GET("comments")
    suspend fun getAll(): List<Comment>
}