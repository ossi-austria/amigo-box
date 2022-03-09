package org.ossiaustria.lib.domain.api

import org.ossiaustria.lib.domain.models.Message
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface MessageApi {

    /**
     * Create a new Message with attached Multimedia/File
     *
     * @param receiverId receiverId (required)
     * @param text text (required)
     * @param file file (optional)
     */
    @Multipart
    @POST("messages")
    suspend fun createMultimediaMessage(
        @Part("receiverId") receiverId: UUID,
        @Part("text") text: UUID
    ): Message

    /**
     * Create a new Message with attached Multimedia/File
     *
     * @param receiverId receiverId (required)
     * @param text text (required)
     */
    @Multipart
    @POST("messages")
    suspend fun createMessage(
        @Part("receiverId") receiverId: UUID,
        @Part("text") text: String,
    ): Message

    /**
     * Filter all Messages for receiver and/or sender
     *
     * @param receiverId receiverId (optional)
     * @param senderId senderId (optional)
     */

    @GET("messages/filter")
    suspend fun getFiltered(
        @Query("receiverId") receiverId: UUID?,
        @Query("senderId") senderId: UUID?
    ): List<Message>

    /**
     * Get one Message
     *
     * @param id id (required)
     */

    @GET("messages/{id}")
    suspend fun getOne(@Path("id") id: UUID): Message

    /**
     * getOwn
     *
     */

    @GET("messages/all")
    suspend fun getOwn(): List<Message>

    /**
     * Get all received Messages
     *
     */

    @GET("messages/received")
    suspend fun getReceived(): List<Message>

    /**
     * Get all sent Messages
     *
     */

    @GET("messages/sent")
    suspend fun getSent(): List<Message>

    /**
     * markAsRetrieved
     *
     * @param id id (required)
     */

    @PATCH("messages/{id}/set-retrieved")
    suspend fun markAsRetrieved(@Path("id") id: UUID): Message
}