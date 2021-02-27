package org.ossiaustria.lib.domain.common

sealed class Outcome<out T>(
    val value: T?
) {

    open val isSuccess: Boolean get() = false
    open val isLoading: Boolean get() = false
    open val isFailure: Boolean get() = false

    // value & exception retrieval

    /**
     * Returns the encapsulated value if this instance represents [success][Outcome.isSuccess] or `null`
     * if it is [failure][Outcome.isFailure].
     *
     * This function is a shorthand for `getOrElse { null }` (see [getOrElse]) or
     * `fold(onSuccess = { it }, onFailure = { null })` (see [fold]).
     */
    fun getOrNull(): T? =
        when {
            isFailure -> null
            else -> value
        }

    /**
     * Returns the encapsulated [Throwable] exception if this instance represents [failure][isFailure] or `null`
     * if it is [success][isSuccess].
     *
     * This function is a shorthand for `fold(onSuccess = { null }, onFailure = { it })` (see [fold]).
     */
    fun exceptionOrNull(): Throwable? =
        when (this) {
            is Failure<*> -> throwable
            else -> null
        }

    /**
     * Returns a string `Success(v)` if this instance represents [success][Outcome.isSuccess]
     * where `v` is a string representation of the value or a string `Failure(x)` if
     * it is [failure][isFailure] where `x` is a string representation of the exception.
     */
    override fun toString(): String =
        when (this) {
            is Failure -> "Failure(${failureCause})"
            is Loading -> "Loading()"
            is Success -> "Success(${value})"
        }

    // companion with constructors

    /**
     * Companion object for [Outcome] class that contains its constructor functions
     * [success] and [failure].
     */
    companion object {
        /**
         * Returns an instance that encapsulates the given [value] as successful value.
         */
        @Suppress("INAPPLICABLE_JVM_NAME")
        @JvmName("success")
        fun <T> success(value: T): Outcome<T> = Success(value)


        @Suppress("INAPPLICABLE_JVM_NAME")
        @JvmName("success")
        fun <T> loading(): Outcome<T> = Loading()

        /**
         * Returns an instance that encapsulates the given [Throwable] [exception] as failure.
         */
        @Suppress("INAPPLICABLE_JVM_NAME")
        @JvmName("failure")
        fun <T> failure(exception: Throwable): Outcome<T> =
            Failure(exception.message ?: "Unknown exception", exception)

        fun <T> failure(message: String): Outcome<T> =
            Failure(message, null)
    }


    class Success<out T> internal constructor(value: T) : Outcome<T>(value) {
        override val isSuccess: Boolean get() = true
    }

    class Loading<out T> internal constructor() : Outcome<T>(null) {
        override val isLoading: Boolean get() = true
    }

    class Failure<out T> internal constructor(
        val failureCause: String,
        val throwable: Throwable? = null, ) : Outcome<T>(null) {
        override val isFailure: Boolean get() = true
    }


}


/**
 * Throws exception if the Outcome is failure. This internal function minimizes
 * inlined bytecode for [getOrThrow] and makes sure that in the future we can
 * add some exception-augmenting logic here (if needed).
 */
@PublishedApi
internal fun Outcome<*>.throwOnFailure() {
    if (this is Outcome.Failure && throwable != null) throw throwable
}

/**
 * Calls the specified function [block] and returns its encapsulated Outcome if invocation was successful,
 * catching any [Throwable] exception that was thrown from the [block] function execution and encapsulating it as a failure.
 */

@Suppress("unused")
inline fun <R> runCatching(block: () -> R): Outcome<R> {
    return try {
        Outcome.success(block())
    } catch (e: Throwable) {
        Outcome.failure(e)
    }
}

/**
 * Calls the specified function [block] with `this` value as its receiver and returns its encapsulated Outcome if invocation was successful,
 * catching any [Throwable] exception that was thrown from the [block] function execution and encapsulating it as a failure.
 */

@Suppress("unused")
inline fun <T, R> T.runCatching(block: T.() -> R): Outcome<R> {
    return try {
        Outcome.success(block())
    } catch (e: Throwable) {
        Outcome.failure(e)
    }
}

// -- extensions ---

/**
 * Returns the encapsulated value if this instance represents [success][Outcome.isSuccess] or throws the encapsulated [Throwable] exception
 * if it is [failure][Outcome.isFailure].
 *
 * This function is a shorthand for `getOrElse { throw it }` (see [getOrElse]).
 */

fun <T> Outcome<T>.getOrThrow(): T? {
    throwOnFailure()
    return value
}


