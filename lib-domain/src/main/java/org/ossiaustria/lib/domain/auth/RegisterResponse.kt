package org.ossiaustria.lib.domain.auth


data class LoginResult(
    val account: Account,
    val refreshToken: TokenResult,
    val accessToken: TokenResult,
)