package org.ossiaustria.lib.domain.api

import org.ossiaustria.lib.domain.models.Message
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

interface MessageApi {

    @GET("messages")
    suspend fun getAll(): List<Message>

    @GET("messages/{id}")
    suspend fun get(@Path("id") id: UUID): Message
}