package org.ossiaustria.lib.domain.common

/**
 * This class represents a Result, which holds data.
 *
 * It can be loading, success or failure
 */
sealed class Resource<out T>() {

    // value & exception retrieval
    val isSuccess: Boolean get() = this is Success
    val isLoading: Boolean get() = this is Loading
    val isFailure: Boolean get() = this is Failure

    open fun valueOrNull(): T? = null

    fun throwableOrNull(): Throwable? =
        when (this) {
            is Failure<*> -> throwable
            else -> null
        }

    /**
     * Returns a string `Success(v)` if this instance represents [success][Resource.isSuccess]
     * where `v` is a string representation of the value or a string `Failure(x)` if
     * it is [failure][isFailure] where `x` is a string representation of the exception.
     */
    override fun toString(): String =
        when (this) {
            is Failure -> "Failure(${failureCause})"
            is Loading -> "Loading()"
            is Success -> "Success(${value})"
        }

    class Success<out T>(val value: T) : Resource<T>() {
        override fun valueOrNull(): T = value
    }

    class Loading<out T> : Resource<T>()

    class Failure<out T>(
        val failureCause: String,
        val throwable: Throwable? = null,
    ) : Resource<T>() {

        constructor(throwable: Throwable) :
                this(throwable.message ?: throwable.toString(), throwable)
    }


    companion object {

        /**
         * Returns an instance that encapsulates the given [value] as successful value.
         */
        @Suppress("INAPPLICABLE_JVM_NAME")
        @JvmName("success")
        fun <T> success(value: T): Resource<T> = Success(value)

        @Suppress("INAPPLICABLE_JVM_NAME")
        @JvmName("success")
        fun <T> loading(): Resource<T> = Loading()

        /**
         * Returns an instance that encapsulates the given [Throwable] [exception] as failure.
         */
        @Suppress("INAPPLICABLE_JVM_NAME")
        @JvmName("failure")
        fun <T> failure(exception: Throwable): Resource<T> =
            Failure(exception.message ?: "Unknown exception", exception)

        fun <T> failure(message: String): Resource<T> =
            Failure(message, null)
    }
}

@PublishedApi
internal fun Resource<*>.throwOnFailure() {
    if (this is Resource.Failure && throwable != null) throw throwable
}

@Suppress("unused")
inline fun <R> runCatching(block: () -> R): Resource<R> {
    return try {
        Resource.success(block())
    } catch (e: Throwable) {
        Resource.failure(e)
    }
}

@Suppress("unused")
inline fun <T, R> T.runCatching(block: T.() -> R): Resource<R> {
    return try {
        Resource.success(block())
    } catch (e: Throwable) {
        Resource.failure(e)
    }
}
