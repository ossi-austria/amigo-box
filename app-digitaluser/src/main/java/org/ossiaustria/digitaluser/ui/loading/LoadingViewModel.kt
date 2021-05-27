package org.ossiaustria.digitaluser.ui.loading

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.ossiaustria.lib.commons.DispatcherProvider
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(dispatcherProvider: DispatcherProvider) :
    ViewModel() {

    val liveUserLogin = MutableLiveData<String>()

    private val ioScope = dispatcherProvider.io()

    fun doFancyHeavyStuffOnBackground() =
        GlobalScope.launch(ioScope) {
            liveUserLogin.postValue("user logged in!")
        }

}