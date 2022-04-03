package org.ossiaustria.lib.jitsi.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import timber.log.Timber

class JitsiWebrtcJsWebView(context: Context, attrs: AttributeSet?) : WebView(context, attrs) {

    interface Listener {
        fun refreshParticipantsCount(count: Int)
        fun refreshAudioMuted(isAudioMuted: Boolean)
    }

    var onConnectionSuccessListener: (() -> Unit)? = null
    var onConnectionFailedListener: (() -> Unit)? = null

    var state = JitsiCurrentCallRoomInfo()

    var listener: Listener? = null
    fun prepare(listener: Listener?) {

        settings.javaScriptEnabled = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.builtInZoomControls = true
        settings.domStorageEnabled = true
        settings.displayZoomControls = false
        settings.mediaPlaybackRequiresUserGesture = false

        settings.loadsImagesAutomatically = true
        settings.blockNetworkLoads = false
        settings.textZoom = 70
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.userAgentString =
            "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36"
        scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        isScrollbarFadingEnabled = true

        setWebContentsDebuggingEnabled(true)
        val cookieManager: CookieManager = CookieManager.getInstance()
        cookieManager.setAcceptThirdPartyCookies(this, true)

        this.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest) {
                request.grant(
                    arrayOf(
                        PermissionRequest.RESOURCE_AUDIO_CAPTURE,
                        PermissionRequest.RESOURCE_VIDEO_CAPTURE
                    )
                )
                Timber.i("JitsiWebrtcJsWebView PermissionRequest $request")
//                request.grant(request.resources)
            }
        }

        this.addJavascriptInterface(this, "JITSI")

        this.listener = listener
        runOnWebView { this.loadUrl("file:///android_asset/jitsi.html") }

    }

    fun keepHidden() {
        this.visibility = View.INVISIBLE
    }

    fun enterRoom(serverUrl: String, jitsiRoom: String?, jwtToken: String?) {
        runOnWebView {
            val measuredWidth = this.width
            val measuredHeight = this.height
            val callJs =
                "javascript:connect(\"$serverUrl\", \"$jitsiRoom\", \"$jwtToken\", $measuredWidth, $measuredHeight);"
            this.loadUrl(callJs)
            this.visibility = View.VISIBLE
        }

    }

    fun runOnWebView(run: WebView.() -> Unit) {
        this.post { this.run() }
    }

    fun finish() = runOnWebView {
        this.loadUrl("javascript:hangup();")
        keepHidden()
    }

    fun toggleAudio() = runOnWebView {
        this.loadUrl("javascript:toggleAudio();")
    }

    fun triggerStatusRefresh() = runOnWebView {
        this.loadUrl("javascript:triggerStatusRefresh();")
    }

    @JavascriptInterface
    fun refreshParticipantsCount(count: Int) {
        state = state.copy(participantsCount = count)
        Timber.i("refreshParticipantsCount -> $state")
        listener?.refreshParticipantsCount(count)
    }

    @JavascriptInterface
    fun refreshAudioMuted(isAudioMuted: Boolean) {
        state = state.copy(isAudioMuted = isAudioMuted)
        Timber.i("refreshAudioMuted -> $state")
        listener?.refreshAudioMuted(isAudioMuted)
    }

    @JavascriptInterface
    fun refreshVideoMuted(isVideoMuted: Boolean) {
        state = state.copy(isVideoMuted = isVideoMuted)
        Timber.i("refreshVideoMuted -> $state")
    }

    @JavascriptInterface
    fun onConnectionSuccess() {
        Timber.i("onConnectionSuccess")
        onConnectionSuccessListener?.invoke()
    }

    @JavascriptInterface
    fun onConnectionFailed() {
        Timber.i("onConnectionFailed")
        onConnectionFailedListener?.invoke()
    }
}