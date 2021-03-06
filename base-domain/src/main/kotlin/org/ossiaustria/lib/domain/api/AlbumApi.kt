package org.ossiaustria.lib.domain.api

import org.ossiaustria.lib.domain.models.Album
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

interface AlbumApi {

    @GET("albums/shared")
    suspend fun getShared(): List<Album>

    @GET("albums/{id}")
    suspend fun get(@Path("id") id: UUID): Album
}