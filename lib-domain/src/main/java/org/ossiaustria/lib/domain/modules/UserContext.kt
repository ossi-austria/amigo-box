package org.ossiaustria.lib.domain.modules

import org.ossiaustria.lib.domain.auth.Account
import org.ossiaustria.lib.domain.auth.AuthInterceptor
import org.ossiaustria.lib.domain.auth.TokenResult
import org.ossiaustria.lib.domain.models.Person
import timber.log.Timber

class UserContext(
    private val authInterceptor: AuthInterceptor
) {

    private var account: Account? = null

    private var person: Person? = null

    fun available() = account != null && person != null
    fun account() = account

    fun accountOrNull() = account
    fun person() = person

    fun personOrNull() = person
    fun accountId() = account()?.id
    fun personId() = person()?.id

    fun initContext(
        accessToken: TokenResult?,
        account: Account?,
        currentPerson: Person?
    ) {
        this.account = account
        this.person = currentPerson ?: account?.persons?.firstOrNull()
        Timber.i("Loaded UserContext for account ${account?.email} and ${person?.id}")

        authInterceptor.clearToken()
        authInterceptor.initToken(accessToken)
        authInterceptor.initPerson(person?.id)
    }
}