package org.ossiaustria.digitaluser

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.ossiaustria.lib.domain.auth.Account
import org.ossiaustria.lib.domain.auth.TokenResult
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.MembershipType
import java.util.*
import java.util.UUID.randomUUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

object EntityMocks {

    fun account(id: UUID = randomUUID(), email: String = "test@example.org"): Account {
        return Account(
            id = id,
            email = email
        )
    }

    fun tokenResult(token: String = "token", subject: String = "subject"): TokenResult {
        return TokenResult(
            token = token,
            subject = subject,
            issuedAt = Date(),
            expiration = Date(),
            issuer = "test",
        )
    }

    fun person(
        personId: UUID,
        groupId: UUID,
        memberType: MembershipType = MembershipType.MEMBER
    ): Person {
        return Person(
            id = personId,
            name = "name",
            email = "email",
            memberType = memberType,
            groupId = groupId
        )
    }
}

/* Copyright 2019 Google LLC.
   SPDX-License-Identifier: Apache-2.0 */
fun <T> LiveData<T>.getOrAwaitValue(
    count: Int = 1,
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    val list = arrayListOf<T>()
    var data: T? = null
    val latch = CountDownLatch(count)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            list.add(o!!)
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}
