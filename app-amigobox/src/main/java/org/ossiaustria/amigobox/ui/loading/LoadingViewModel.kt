package org.ossiaustria.amigobox.ui.loading

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(


) : ViewModel() {

    val liveUserLogin = MutableLiveData<String>()

    // void doFancyHeavyStuffOnBackground()
    fun doFancyHeavyStuffOnBackground() {
        GlobalScope.launch(Dispatchers.IO) {
            Thread.sleep(5000)
            liveUserLogin.postValue("user logged in!")
        }
    }

}