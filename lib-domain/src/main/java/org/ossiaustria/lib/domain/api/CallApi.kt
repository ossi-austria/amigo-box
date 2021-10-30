package org.ossiaustria.lib.domain.api

import org.ossiaustria.lib.domain.models.Call
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface CallApi {
    /**
     * Accept an incoming Call
     * The endpoint is owned by kotlincoroutines service owner
     * @param id id (required)
     */
    @PATCH("calls/{id}/accept")
    suspend fun acceptCall(
        @Path("id") id: UUID,
    ): Call

    /**
     * Cancel an existing Call
     * The endpoint is owned by kotlincoroutines service owner
     * @param id id (required)
     */
    @PATCH("calls/{id}/cancel")
    suspend fun cancelCall(
        @Path("id") id: UUID,
    ): Call

    /**
     * Create a new Call
     * The endpoint is owned by kotlincoroutines service owner
     * @param callType callType (required)
     * @param receiverId receiverId (required)
     */

    @POST("calls")
    suspend fun createCall(
        @Query("callType") callType: String,
        @Query("receiverId") receiverId: UUID,
    ): Call

    /**
     * Deny an incoming Call
     * The endpoint is owned by kotlincoroutines service owner
     * @param id id (required)
     */

    @PATCH("calls/{id}/deny")
    suspend fun denyCall(
        @Path("id") id: UUID,
    ): Call

    /**
     * Finish an active Call
     * The endpoint is owned by kotlincoroutines service owner
     * @param id id (required)
     */

    @PATCH("calls/{id}/finish")
    suspend fun finishCall(
        @Path("id") id: UUID,
    ): Call

    /**
     * Filter all Calls for receiver and/or sender
     * The endpoint is owned by kotlincoroutines service owner
     * @param receiverId receiverId (optional)
     * @param senderId senderId (optional)
     */
    @GET("calls/filter")
    suspend fun getFiltered(
        @Query("receiverId") receiverId: UUID?,
        @Query("senderId") senderId: UUID?
    ): List<Call>

    /**
     * Get one Call
     * The endpoint is owned by kotlincoroutines service owner
     * @param id id (required)
     */
    @GET("calls/{id}")
    suspend fun getOne(
        @Path("id") id: UUID,
    ): Call

    /**
     * Get all Calls
     * The endpoint is owned by kotlincoroutines service owner
     */
    @GET("calls/all")
    suspend fun getOwn(): List<Call>

    /**
     * Get all received Calls
     * The endpoint is owned by kotlincoroutines service owner
     */
    @GET("calls/received")
    suspend fun getReceived(): List<Call>

    /**
     * Get all sent Calls
     * The endpoint is owned by kotlincoroutines service owner
     */
    @GET("calls/sent")
    suspend fun getSent(): List<Call>
}