package org.ossiaustria.lib.domain.services.events

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.repositories.CallRepository
import timber.log.Timber
import java.util.*

class IncomingEventCallbackServiceImpl(
    ioDispatcher: CoroutineDispatcher,
    private val callRepository: CallRepository
) : IncomingEventCallbackService {

    private val scope = CoroutineScope(ioDispatcher)

    private val _callEventFlow = MutableSharedFlow<Call>()
    override val callEventFlow: SharedFlow<Call> = _callEventFlow

    override fun handleCall(id: UUID): Resource<Call> {
        // Download Call: needed for everything.
        val callResource = runBlocking(scope.coroutineContext) {
            callRepository.getCall(id, true).first { it !is Resource.Loading }
        }
        return callResource
    }

    override fun handleCloudEventCall(cloudEvent: AmigoCloudEvent): Boolean {
        Timber.i("Handle cloudEvent: $cloudEvent")
        Timber.i("Handle cloudEvent: ${cloudEvent.type}")
        Timber.i("Handle cloudEvent subscriptionCount:${_callEventFlow.subscriptionCount.value}")

        // Download Call: needed for everything.
        val callResource = runBlocking(scope.coroutineContext) {
            callRepository.getCall(cloudEvent.entityId, true).first { it !is Resource.Loading }
        }
        return when (callResource) {
            is Resource.Success -> {
                val call = callResource.value
                Timber.i("Handle cloudEvent: Resource.Success: $call")
                /**
                 * When no Observer was connected, e.g. when App is in Background or was killed,
                 * we need to perform the fallbackAction (start Activity!)
                 */
                return if (_callEventFlow.subscriptionCount.value > 0) {
                    scope.launch {
                        _callEventFlow.emit(call)
                    }
                    true
                } else false
            }
            is Resource.Failure -> {
                Timber.w("Handle cloudEvent: Cannot retrieve call of ")
                Timber.e(callResource.throwable, "Error during handling $cloudEvent")
                return false
            }
            else -> return false
        }
    }
}