package org.ossiaustria.amigobox.ui.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.ossiaustria.lib.domain.models.Person
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor() : ViewModel() {

    private val _persons: MutableLiveData<List<Person>> = MutableLiveData()
    val persons: LiveData<List<Person>> = _persons

    fun load() {
        _persons.value = ContactsSourceMockData.listOfPeopleWithImages()
    }

}