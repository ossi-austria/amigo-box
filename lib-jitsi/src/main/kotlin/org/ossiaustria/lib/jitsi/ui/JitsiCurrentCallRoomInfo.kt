package org.ossiaustria.lib.jitsi.ui

data class JitsiCurrentCallRoomInfo(
    val participantsCount: Int = 0,
    val isAudioMuted: Boolean = false,
    val isVideoMuted: Boolean = false,
) {
}