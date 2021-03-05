package org.ossiaustria.lib.commons

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatcherProvider {
    fun default(): CoroutineDispatcher
    fun io(): CoroutineDispatcher
    fun main(): CoroutineDispatcher
    fun unconfined(): CoroutineDispatcher
}

class DefaultDispatcherProvider : DispatcherProvider {
    override fun default(): CoroutineDispatcher = Dispatchers.Default
    override fun io(): CoroutineDispatcher = Dispatchers.IO
    override fun main(): CoroutineDispatcher = Dispatchers.Main
    override fun unconfined(): CoroutineDispatcher = Dispatchers.Unconfined
}

class TestDispatcherProvider(private val dispatcher: CoroutineDispatcher) : DispatcherProvider {
    override fun io(): CoroutineDispatcher = dispatcher
    override fun main(): CoroutineDispatcher = dispatcher
    override fun default(): CoroutineDispatcher = dispatcher
    override fun unconfined(): CoroutineDispatcher = dispatcher

}