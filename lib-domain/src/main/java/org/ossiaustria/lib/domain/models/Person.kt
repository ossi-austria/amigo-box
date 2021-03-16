package org.ossiaustria.lib.domain.models

import org.ossiaustria.lib.domain.models.enums.MembershipType
import java.util.*

/**
 * Domain class for Person.
 * Uses MemberEntity and GroupEntity for data mapping
 */
data class Person(
    val id: UUID,
    val name: String,
    val email: String,
    val memberType: MembershipType,
    val groupId: UUID?
)