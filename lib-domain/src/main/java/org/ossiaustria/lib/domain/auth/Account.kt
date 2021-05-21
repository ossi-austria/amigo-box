package org.ossiaustria.lib.domain.auth

import org.ossiaustria.lib.domain.models.Person
import java.util.*


data class Account(
    val id: UUID,
    val email: String,
    val changeAccountToken: String? = null,
    val changeAccountTokenCreatedAt: Date? = null,
    val persons: List<Person> = listOf(),
)