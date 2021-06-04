package org.ossiaustria.lib.domain.auth

import timber.log.Timber

class AuthInterceptor {

    var currentToken: TokenResult? = null

    fun currentToken(): TokenResult? {
        return currentToken
    }

    fun initToken(token: TokenResult?) {
        if (token != null) {
            Timber.i("init with access token issued at ${token.issuedAt}")
            currentToken = token
        } else {
            Timber.w("empty token probably not yet authenticated")
        }
    }

    fun clearToken() {
        currentToken = null
    }
}