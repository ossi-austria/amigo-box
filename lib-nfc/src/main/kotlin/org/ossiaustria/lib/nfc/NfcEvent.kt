package org.ossiaustria.lib.nfc

sealed class NfcEvent(val type: NfcEventType) {
    data class TagFound(val data: NfcTagData) : NfcEvent(NfcEventType.TAG_FOUND)
    object TagLost : NfcEvent(NfcEventType.TAG_LOST)
    data class Error(val error: String) : NfcEvent(NfcEventType.ERROR)
    object Undefined : NfcEvent(NfcEventType.UNDEFINED)
}