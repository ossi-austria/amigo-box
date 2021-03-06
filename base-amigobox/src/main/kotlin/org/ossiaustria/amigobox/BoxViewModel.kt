package org.ossiaustria.amigobox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.cancelChildren

abstract class BoxViewModel(
    protected val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    /**
     * When ViewModel gets destroyed, cancel the remaining open coroutine jobs
     */
    override fun onCleared() {
        super.onCleared()
        viewModelScope.coroutineContext.cancelChildren()
    }

}