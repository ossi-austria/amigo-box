package org.ossiaustria.lib.domain.auth

import timber.log.Timber
import java.util.*

class AuthInterceptor {

    private var currentToken: TokenResult? = null
    private var personId: UUID? = null

    fun currentToken(): TokenResult? {
        return currentToken
    }

    fun personId(): UUID? {
        return personId
    }

    fun initToken(token: TokenResult?) {
        if (token != null) {
            Timber.i("init with access token issued at ${token.issuedAt}")
            currentToken = token
        } else {
            Timber.w("empty token probably not yet authenticated")
        }
    }

    fun initPerson(personId: UUID?) {
        if (personId != null) {
            Timber.i("init with access token")
            this.personId = personId
        } else {
            Timber.w("empty token probably not yet authenticated")
        }
    }

    fun clearToken() {
        Timber.w("clearToken")
        currentToken = null
        personId = null
    }
}