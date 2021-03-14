package org.ossiaustria.lib.domain.repositories

import com.dropbox.android.external.store4.StoreResponse
import kotlinx.coroutines.flow.FlowCollector
import org.ossiaustria.lib.domain.common.Outcome
import timber.log.Timber

abstract class AbstractRepository {
    protected suspend fun <T> FlowCollector<Outcome<T>>.transformResponseToOutcome(
        response: StoreResponse<T>,
        onNewData: () -> Outcome<T>
    ) {
        when (response) {
            is StoreResponse.Loading -> {
                Timber.d("[Store 4] Loading from ${response.origin}")
                emit(Outcome.loading())
            }

            is StoreResponse.Error -> {
                Timber.d("[Store 4] Error from ${response.origin}")
                emit(
                    Outcome.failure(response.errorMessageOrNull() ?: "Store 4 Error")
                )
            }
            is StoreResponse.Data -> {
                val data = response.value
                Timber.d("[Store 4] Data from ${response.origin}")
                emit(Outcome.success(data))
            }
            is StoreResponse.NoNewData -> {
                Timber.d("[Store 4] NoNewData from ${response.origin}")
                // either use an empty list, fetch from dao, or just signify "still loading" here
                emit(onNewData.invoke())
            }
            else -> {
                throw IllegalStateException("State lost.")
            }
        }
    }
}