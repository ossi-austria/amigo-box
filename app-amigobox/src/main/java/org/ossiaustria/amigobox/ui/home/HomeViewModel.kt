package org.ossiaustria.amigobox.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.ossiaustria.lib.domain.common.Effect
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.repositories.AlbumRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private var albumRepository: AlbumRepository
) : ViewModel() {

    private val _liveAlbums = MutableLiveData<List<Album>>()
    val liveAlbums: LiveData<List<Album>> get() = _liveAlbums

    init {
        viewModelScope.launch {
            albumRepository.getAllAlbums().collect { result ->
                when (result) {
                    is Effect.Loading -> Timber.i("Loading")
                    is Effect.Failure -> Timber.i(result.failureCause)
                    is Effect.Success -> _liveAlbums.postValue(result.value!!)
                }
            }
        }
    }
}