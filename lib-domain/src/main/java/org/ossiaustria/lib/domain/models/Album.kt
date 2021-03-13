package org.ossiaustria.lib.domain.models

import java.util.*


data class Album(
    val id: UUID,

    val createdAt: Long,
    val updatedAt: Long,

    val name: String,
    val owner: Person,
    val items: List<Multimedia>
)