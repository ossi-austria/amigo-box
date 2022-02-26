package org.ossiaustria.lib.domain.api

import org.ossiaustria.lib.domain.models.Person
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

interface PersonApi {

    @GET("persons")
    suspend fun getAll(): List<Person>

    @GET("persons/{id}")
    suspend fun get(@Path("id") id: UUID): Person

}