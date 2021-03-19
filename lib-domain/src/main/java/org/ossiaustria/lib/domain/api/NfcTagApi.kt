package org.ossiaustria.lib.domain.api

import org.ossiaustria.lib.domain.models.NfcTag
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

interface NfcTagApi {

    @GET("nfcs")
    suspend fun getAll(): List<NfcTag>

    @GET("nfcs/{id}")
    suspend fun get(@Path("id") id: UUID): NfcTag
}