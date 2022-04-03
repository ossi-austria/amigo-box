package org.ossiaustria.amigobox.cloudmessaging

/**
 * Provides a static name of the Main entrypoint for FCM intents and other intents.
 * Must be overriden in app-amigobox and app-amigobox-pro
 */
interface IntentEntryPointProvider {

    fun getMainClass(): Class<*>
}