package org.ossiaustria.amigobox.nfc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.NfcInfo
import org.ossiaustria.lib.domain.services.NfcInfoService
import org.ossiaustria.lib.nfc.NfcHandler
import timber.log.Timber

class NfcViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val nfcInfoService: NfcInfoService,
) : ViewModel() {

    private val _state: MutableLiveData<Resource<NfcInfo>> = MutableLiveData()
    val state: LiveData<Resource<NfcInfo>> = _state

    fun onResume() {
        viewModelScope.launch(ioDispatcher) {
            nfcInfoService.getAll().collect {
                Timber.i("Loading all NFCs: ${it.valueOrNull()?.size}")
            }
        }
    }

    fun processNfcInfo(nfcTagData: NfcHandler.NfcTagData) {
        val ref = nfcTagData.tagId
        viewModelScope.launch(ioDispatcher) {
            nfcInfoService.findPerRef(ref).collect { result ->
                _state.postValue(result)
            }
        }
    }
}