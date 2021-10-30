package org.ossiaustria.lib.domain.api

import org.ossiaustria.lib.domain.models.Group
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface GroupApi : Api {

    @GET("groups")
    suspend fun getOwn(): List<Group>

    @GET("groups/{id}")
    suspend fun get(@Path("id") id: UUID): Group

    /**
     * Search/filter visible Groups
     * The endpoint is owned by kotlincoroutines service owner
     * @param name name (optional)
     * @param personId personId (optional)
     */

    @GET("groups/filtered")
    suspend fun filterGroups(
        @Query("name") name: String?,
        @Query("personId") personId: UUID?
    ): List<Group>
}