package org.ossiaustria.lib.domain.models

import org.ossiaustria.lib.domain.models.enums.MemberType
import java.io.Serializable
import java.util.*

/**
 * Domain class for Person.
 * Uses MemberEntity and GroupEntity for data mapping
 */
data class Person(
    val id: UUID,
    val name: String,
    val groupId: UUID,
    val memberType: MemberType,
    // email is null for every person, which is not the user themself
    val email: String? = null,
    val avatarUrl: String? = null
) : Serializable