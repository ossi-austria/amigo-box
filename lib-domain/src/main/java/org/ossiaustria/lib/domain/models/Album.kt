package org.ossiaustria.lib.domain.models

import java.io.Serializable
import java.util.*

data class Album(
    val id: UUID,

    val name: String,
    val ownerId: UUID,
    val items: List<Multimedia>,

    val createdAt: Date = Date(),
    val updatedAt: Date? = null

) : Serializable