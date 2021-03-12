package org.ossiaustria.lib.domain.models

import java.util.*

enum class MembershipType {
    MEMBER,
    ADMIN,
    CENTER
}

data class Person(
    val id: UUID,
    val name: String,
    val email: String,
    val memberType: MembershipType
)