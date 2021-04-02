package org.ossiaustria.lib.domain.api

import org.ossiaustria.lib.domain.models.AlbumShare
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

interface AlbumShareApi {

    @GET("shares")
    suspend fun getAll(): List<AlbumShare>

    @GET("shares/{id}")
    suspend fun get(@Path("id") id: UUID): AlbumShare
}