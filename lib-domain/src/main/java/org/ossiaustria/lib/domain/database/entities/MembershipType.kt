package org.ossiaustria.lib.domain.database.entities

internal enum class MembershipType {
    MEMBER,
    ADMIN,
    CENTER
}

internal fun MembershipType.toEnum(): org.ossiaustria.lib.domain.models.MembershipType =
    when (this) {
        MembershipType.MEMBER -> org.ossiaustria.lib.domain.models.MembershipType.MEMBER
        MembershipType.ADMIN -> org.ossiaustria.lib.domain.models.MembershipType.ADMIN
        MembershipType.CENTER -> org.ossiaustria.lib.domain.models.MembershipType.CENTER
    }

