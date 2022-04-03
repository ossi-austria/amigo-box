package org.ossiaustria.amigobox

import org.ossiaustria.amigobox.MainBoxActivity
import org.ossiaustria.amigobox.cloudmessaging.IntentEntryPointProvider

class AmigoBoxIntentEntryPointProvider : IntentEntryPointProvider {

    override fun getMainClass(): Class<*> {
        return MainBoxActivity::class.java
    }
}