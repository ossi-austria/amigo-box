package org.ossiaustria.lib.domain.modules

import org.ossiaustria.lib.domain.auth.Account
import org.ossiaustria.lib.domain.models.Person
import timber.log.Timber

class UserContext {

    private var account: Account? = null

    private var person: Person? = null

    fun available() = account != null && person != null
    fun account() = account

    fun accountOrNull() = account
    fun person() = person

    fun personOrNull() = person
    fun accountId() = account()?.id
    fun personId() = person()?.id

    fun initContext(account: Account?) {
        this.account = account
        this.person = account?.persons?.firstOrNull()
        Timber.i("Loaded UserContext for account ${account?.email} and ${person?.id}")
    }
}