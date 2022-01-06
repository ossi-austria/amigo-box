package org.ossiaustria.lib.domain.services.events

import kotlinx.coroutines.flow.SharedFlow
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Call
import java.util.*


/**
 * Handles all FCM CloudEvents and local Jitsi Broadcast events
 */
interface IncomingEventCallbackService {

    val callEventFlow: SharedFlow<Call>

    /**
     * This method performs the necessary checks to know, whether we can run in foreground (have connected ViewModels and UI)
     * or we start from background or even a killed state.
     *
     * With *fallbackAction* it is asserted, that any time something is executed.
     * Either
     *   a) A IncomingEventsViewModel is connected and has Observers -> notifiedCall will be published
     *   b) IncomingEventsViewModel will not handle the call, then *fallbackAction* is going to be executed.
     *
     * @param cloudEvent AmigoCloudEvent is a parsed FCM event which has necessary Data
     * @param fallbackAction Is an action which can handle the Call when on background
     */
    fun handleCloudEventCall(cloudEvent: AmigoCloudEvent): Boolean
    fun handleCall(id: UUID): Resource<Call>
}