package org.ossiaustria.lib.domain.auth

import java.util.*

data class TokenResult(
    val token: String,
    val subject: String,
    val issuedAt: Date,
    val expiration: Date,
    val issuer: String
)