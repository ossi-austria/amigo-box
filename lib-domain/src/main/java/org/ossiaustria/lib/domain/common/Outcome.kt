package org.ossiaustria.lib.domain.common

/**
 * This class represents a Result, which holds data.
 *
 * It can be loading, success or failure
 */
sealed class Outcome<out T>(
    private val status: Int,
    val value: T?
) {

    val isSuccess: Boolean get() = status == STATUS_SUCCESS
    val isLoading: Boolean get() = status == STATUS_LOADING
    val isFailure: Boolean get() = status == STATUS_ERROR

    // value & exception retrieval

    fun getOrNull(): T? =
        when {
            isFailure -> null
            else -> value
        }

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

    companion object {

        private const val STATUS_LOADING = 0
        private const val STATUS_SUCCESS = 1
        private const val STATUS_ERROR = -1

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


    class Success<out T> internal constructor(value: T) : Outcome<T>(STATUS_SUCCESS, value)
    class Loading<out T> internal constructor() : Outcome<T>(STATUS_LOADING, null)
    class Failure<out T> internal constructor(
        val failureCause: String,
        val throwable: Throwable? = null, ) : Outcome<T>(STATUS_ERROR, null)

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


