package org.ossiaustria.amigobox.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.modules.UserContext
import org.ossiaustria.lib.domain.repositories.AlbumRepository
import timber.log.Timber

class HomeViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val albumRepository: AlbumRepository,
    private val userContext: UserContext

) : ViewModel() {

    private val _liveAlbums = MutableLiveData<List<Album>>()
    val liveAlbums: LiveData<List<Album>> = _liveAlbums

    val name by lazy { userContext.person()?.name ?: "EMMA" }

    init {
        viewModelScope.launch {
            albumRepository.getAllAlbums().collect { result ->
                when (result) {
                    is Resource.Loading -> Timber.i("Loading")
                    is Resource.Failure -> Timber.i(result.failureCause)
                    is Resource.Success -> result.value.let { _liveAlbums.postValue(it) }
                }
            }
        }
    }
}