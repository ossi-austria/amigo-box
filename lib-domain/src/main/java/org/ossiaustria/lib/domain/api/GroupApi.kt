package org.ossiaustria.lib.domain.api

import org.ossiaustria.lib.domain.models.Group
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

interface GroupApi : Api {

    @GET("groups/my")
    suspend fun getOwn(): List<Group>

    @GET("groups/{id}")
    suspend fun get(@Path("id") id: UUID): Group
}