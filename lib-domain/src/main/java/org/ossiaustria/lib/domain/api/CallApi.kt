package org.ossiaustria.lib.domain.api

import org.ossiaustria.lib.domain.models.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

interface CallApi {

    @GET("calls/all")
    suspend fun getAll(): List<Call>

    @GET("calls/{id}")
    suspend fun get(@Path("id") id: UUID): Call
}