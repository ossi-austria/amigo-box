package org.ossiaustria.lib.domain.api

import org.ossiaustria.lib.domain.api.Api
import org.ossiaustria.lib.domain.models.Comment
import retrofit2.http.GET

interface CommentsApi : Api {

    @GET("comments")
    suspend fun getAll(): List<Comment>
}