package org.ossiaustria.lib.domain.models

import org.ossiaustria.lib.domain.models.enums.MemberType
import java.util.*

data class Group(

    val id: UUID,
    val name: String,
    val members: List<Person>,
) {
    val centerPerson: Person? =
        members.firstOrNull { it.memberType == MemberType.ANALOGUE }

    val admins: List<Person> =
        members.filter { it.memberType == MemberType.ADMIN }
}