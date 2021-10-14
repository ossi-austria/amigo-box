package org.ossiaustria.lib.domain.models

import org.ossiaustria.lib.domain.models.enums.MultimediaType
import java.io.Serializable
import java.util.*

/**
 *   "id" : "d94f2803-d410-49b5-b476-1c0df8706f01",
"ownerId" : "d97a528c-34c1-4a95-801e-34c3fd9eb68d",
"filename" : "filename",
"contentType" : null,
"createdAt" : "2021-10-07T06:57:26.098Z",
"type" : "IMAGE",
"size" : null,
"albumId" : null
 */
data class Multimedia(

    val id: UUID,
    val ownerId: UUID,
    val filename: String,
    val createdAt: Date = Date(),

    val contentType: String,
    val type: MultimediaType,
    val size: Long? = null,
    val albumId: UUID? = null,

    ) : Serializable