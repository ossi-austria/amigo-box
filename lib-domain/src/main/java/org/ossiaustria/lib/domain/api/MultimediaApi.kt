package org.ossiaustria.lib.domain.api

import org.ossiaustria.lib.domain.models.Multimedia
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

interface MultimediaApi {

    @GET("multimedias/shared")
    suspend fun getShared(): List<Multimedia>

    @GET("multimedias/{id}")
    suspend fun getOne(@Path("id") id: UUID): Multimedia

    @GET("multimedias/{id}/file")
    suspend fun downloadFile(@Path("id") id: UUID): Multimedia
}