package org.ossiaustria.lib.domain.api

import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.AmigoNfcInfo
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

interface NfcInfoApi {

    @GET("nfcs/own")
    suspend fun getAllAccessibleNfcs(): List<AmigoNfcInfo>

    @GET("nfcs/albums")
    suspend fun getAllLinkedAlbums(): List<Album>

    @GET("nfcs/{id}")
    suspend fun getOne(@Path("id") id: UUID): AmigoNfcInfo
}