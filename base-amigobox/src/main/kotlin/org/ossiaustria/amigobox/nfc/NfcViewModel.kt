package org.ossiaustria.amigobox.nfc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.nfc.NfcViewModelEvent.CallPerson
import org.ossiaustria.amigobox.nfc.NfcViewModelEvent.Failure
import org.ossiaustria.amigobox.nfc.NfcViewModelEvent.OpenAlbum
import org.ossiaustria.amigobox.nfc.NfcViewModelEvent.TagLost
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.AmigoNfcInfo
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.NfcTagType
import org.ossiaustria.lib.domain.repositories.AlbumRepository
import org.ossiaustria.lib.domain.repositories.PersonRepository
import org.ossiaustria.lib.domain.services.NfcInfoService
import org.ossiaustria.lib.nfc.NfcEvent
import java.util.*

sealed class NfcViewModelEvent(val success: Boolean) {
    data class Error(val exception: Throwable) : NfcViewModelEvent(false)
    data class Failure(val message: String) : NfcViewModelEvent(false)
    data class CallPerson(val person: Person) : NfcViewModelEvent(true)
    data class OpenAlbum(val album: Album) : NfcViewModelEvent(true)
    class TagLost : NfcViewModelEvent(false)
}

class NfcViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val nfcInfoService: NfcInfoService,
    private val albumRepository: AlbumRepository,
    private val personRepository: PersonRepository,
) : ViewModel() {

//    private val _amigoNfcInfo: MutableLiveData<Resource<AmigoNfcInfo>> = MutableLiveData()
//    val amigoNfcInfo: LiveData<Resource<AmigoNfcInfo>> = _amigoNfcInfo

    private val _nfcEvent: MutableLiveData<NfcViewModelEvent> = MutableLiveData()
    val nfcEvent: LiveData<NfcViewModelEvent> = _nfcEvent

    /**
     * Processes the internal Nfc Read result and prepares this ViewModel for loading
     */
    fun processNfcTagData(nfcEvent: NfcEvent) {

        if (nfcEvent is NfcEvent.TagFound && nfcEvent.data.tagId != null) {
            viewModelScope.launch(ioDispatcher) {
                val result = nfcInfoService.findPerRef(nfcEvent.data.tagId!!)
                if (result is Resource.Success) {
                    handleNfcInfo(result.value)
                } else if (result is Resource.Failure) {
                    _nfcEvent.postValue(Failure(result.failureCause))
                }
            }
        } else {
            val amigoNfcEvent = when (nfcEvent) {
                is NfcEvent.TagLost -> TagLost()
                else -> Failure("Cannot process nfcEvent: $nfcEvent")
            }
            _nfcEvent.postValue(amigoNfcEvent)
//            _amigoNfcInfo.postValue(Resource.failure("NfcTagData cannot be used: $nfcEvent"))
        }
    }

    fun handleNfcInfo(amigoNfcInfo: AmigoNfcInfo) {
        viewModelScope.launch(ioDispatcher) {
            when (amigoNfcInfo.type) {
                NfcTagType.CALL_PERSON -> loadPerson(amigoNfcInfo.linkedPersonId)
                NfcTagType.OPEN_ALBUM -> loadAlbum(amigoNfcInfo.linkedAlbumId)
                else -> _nfcEvent.postValue(Failure("handleNfcInfo: do nothing for ${amigoNfcInfo.type}"))
            }
        }
    }

    private suspend fun loadAlbum(linkedAlbumId: UUID?) {
        if (linkedAlbumId != null) {
            val resource = albumRepository.getAlbum(linkedAlbumId).take(2).last()
            if (resource.isSuccess) {
                _nfcEvent.postValue(OpenAlbum(resource.valueOrNull()!!))
            } else if (resource.isFailure) {
                _nfcEvent.postValue(Failure("Cannot load Album"))
            }
        }
    }

    private suspend fun loadPerson(linkedPersonId: UUID?) {
        if (linkedPersonId != null) {
            val resource = personRepository.getPerson(linkedPersonId).take(2).last()
            if (resource.isSuccess) {
                _nfcEvent.postValue(CallPerson(resource.valueOrNull()!!))
            } else if (resource.isFailure) {
                _nfcEvent.postValue(Failure("Cannot load Person"))
            }
        }
    }

    fun openAlbum(linkedAlbum: Album, navigator: Navigator) {
        navigator.toImageGallery(linkedAlbum)
    }

    fun callPerson(linkedPerson: Person, navigator: Navigator) {
        navigator.toCallFragment(linkedPerson)
    }
}