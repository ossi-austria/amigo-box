package org.ossiaustria.amigobox.nfc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.NfcInfo
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.NfcTagType
import org.ossiaustria.lib.domain.repositories.AlbumRepository
import org.ossiaustria.lib.domain.repositories.PersonRepository
import org.ossiaustria.lib.domain.services.NfcInfoService
import org.ossiaustria.lib.nfc.NfcHandler
import timber.log.Timber
import java.util.*

sealed class NfcViewModelState {
    data class Error(val exception: Throwable) : NfcViewModelState()
    data class CallPerson(val person: Person) : NfcViewModelState()
    data class OpenAlbum(val album: Album) : NfcViewModelState()
}

class NfcViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val nfcInfoService: NfcInfoService,
    private val albumRepository: AlbumRepository,
    private val personRepository: PersonRepository,
) : ViewModel() {

    private val _nfcInfo: MutableLiveData<Resource<NfcInfo>> = MutableLiveData()
    val nfcInfo: LiveData<Resource<NfcInfo>> = _nfcInfo

    private val _state: MutableLiveData<Resource<NfcViewModelState>> = MutableLiveData()
    val state: LiveData<Resource<NfcViewModelState>> = _state

    fun processNfcTagData(nfcTagData: NfcHandler.NfcTagData) {
        val ref = nfcTagData.tagId
        viewModelScope.launch(ioDispatcher) {
            nfcInfoService.findPerRef(ref).collectLatest { result ->
                if (!result.isLoading) {
                    _nfcInfo.postValue(result)
                }
            }
        }
    }

    fun handleNfcInfo(nfcInfo: NfcInfo) {
        viewModelScope.launch(ioDispatcher) {
            when (nfcInfo.type) {
                NfcTagType.CALL_PERSON -> loadPerson(nfcInfo.linkedPersonId)
                NfcTagType.OPEN_ALBUM -> loadAlbum(nfcInfo.linkedAlbumId)
                else -> Timber.i("handleNfcInfo: do nothing for ${nfcInfo.type}")
            }
        }
    }

    private suspend fun loadAlbum(linkedAlbumId: UUID?) {
        if (linkedAlbumId != null) {
            albumRepository.getAlbum(linkedAlbumId).collectLatest {
                if (it.isSuccess) {
                    _state.postValue(Resource.success(NfcViewModelState.OpenAlbum(it.valueOrNull()!!)))
                } else if (it.isFailure) {
                    _state.postValue(Resource.failure("Cannot load Album"))
                }
            }
        }
    }

    private suspend fun loadPerson(linkedPersonId: UUID?) {
        if (linkedPersonId != null) {
            personRepository.getPerson(linkedPersonId).collectLatest {
                if (it.isSuccess) {
                    _state.postValue(Resource.success(NfcViewModelState.CallPerson(it.valueOrNull()!!)))
                } else if (it.isFailure) {
                    _state.postValue(Resource.failure("Cannot load Person"))
                }
            }
        }
    }

    fun openAlbum(linkedAlbum: Album, navigator: Navigator) {
        navigator.toImageGallery(linkedAlbum)
    }

    fun callPerson(linkedPerson: Person, navigator: Navigator) {
        navigator.toCallPerson(linkedPerson)
    }
}