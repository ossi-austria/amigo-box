package org.ossiaustria.amigobox.ui.commons

import androidx.lifecycle.ViewModel
import org.ossiaustria.amigobox.Navigator

open class NavigationViewModel : ViewModel() {

    protected var navigator: Navigator? = null

    fun bind(navigator: Navigator) {
        this.navigator = navigator
    }

    fun unbind() = onCleared()

    override fun onCleared() {
        navigator = null
        super.onCleared()
    }
}
