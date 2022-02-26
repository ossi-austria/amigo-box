package org.ossiaustria.lib.domain.api

import org.ossiaustria.lib.domain.models.Sendable
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

interface SendableApi {

    @GET("sendables")
    suspend fun getAll(): List<Sendable>

    @GET("sendables/{id}")
    suspend fun get(@Path("id") id: UUID): Sendable
}