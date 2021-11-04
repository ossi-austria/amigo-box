package org.ossiaustria.amigobox.ui.commons

import androidx.lifecycle.ViewModel
import org.ossiaustria.amigobox.Navigator

abstract class NavigationViewModel : ViewModel() {

    protected var navigator: Navigator? = null

    fun bind(navigator: Navigator) {
        this.navigator = navigator
    }

    override fun onCleared() {
        navigator = null
        super.onCleared()
    }
}
