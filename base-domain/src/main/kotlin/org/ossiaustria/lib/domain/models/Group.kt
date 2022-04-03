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

    /**
     * All members, who are not the ANALOGUE themselves, meaning "all others" but exluding the user
     */
    val digitals: List<Person> =
        members.filter { it.memberType != MemberType.ANALOGUE }
}