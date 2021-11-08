package org.ossiaustria.lib.domain.models.enums

enum class CallType {
    VIDEO,
    AUDIO,
}

enum class CallState {
    @Deprecated("Use CALLING instead")
    CREATED,
    CALLING,
    CANCELLED, DENIED, TIMEOUT,
    ACCEPTED, STARTED,
    FINISHED,
}