package org.ossiaustria.amigobox.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import org.ossiaustria.amigobox.BoxViewModel
import org.ossiaustria.lib.domain.models.Person
import javax.inject.Inject

@HiltViewModel
class GlobalStateViewModel @Inject constructor(
    ioDispatcher: CoroutineDispatcher,
) : BoxViewModel(ioDispatcher) {

    private val _selectedPerson = MutableLiveData<Person>(null)
    val selectedPerson: LiveData<Person> = _selectedPerson

    fun setCurrentPerson(person: Person) {
        _selectedPerson.value = person
    }


}