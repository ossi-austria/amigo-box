package org.ossiaustria.lib.domain.models

import java.util.*


data class Group(

    val id: UUID,
    val name: String,
    val centerPerson: Person?,
    val members: List<Person>,
    val admins: List<Person>
)