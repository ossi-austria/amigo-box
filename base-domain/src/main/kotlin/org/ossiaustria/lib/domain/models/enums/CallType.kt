package org.ossiaustria.lib.domain.models.enums

enum class CallType {
    VIDEO,
    AUDIO,
}

enum class CallState {
    CALLING, // Notification sent, should display Calling window
    CANCELLED, // caller cancels
    DENIED, // callee denies/cancels
    ACCEPTED, // callee accepted
    FINISHED, // caller/callee finishes
    TIMEOUT, // timeout or technical timeout
}
