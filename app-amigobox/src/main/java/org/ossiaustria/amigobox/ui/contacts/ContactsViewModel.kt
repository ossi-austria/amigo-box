package org.ossiaustria.amigobox.ui.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.ui.contacts.ContactsSourceMockData.listOfPeopleWithImages
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.repositories.GroupRepository

class ContactsViewModel(
    private val groupRepository: GroupRepository,
    private val navigator: Navigator
) : ViewModel() {

    private val _persons: MutableLiveData<List<Person>> = MutableLiveData()
    val persons: LiveData<List<Person>> = _persons

    fun load() {
        viewModelScope.launch {
            val first = groupRepository.getAllGroups().first()
            if (first.isSuccess && !first.valueOrNull().isNullOrEmpty()) {
                val groups = first.valueOrNull()
                groups?.flatMap { it.members }?.let {
                    _persons.value = it
                }
            } else {
                _persons.value = getPersons()
            }
        }
    }

    private fun getPersons(): List<Person> = listOfPeopleWithImages()

    fun backToHome() {
        navigator.toHome()
    }

    fun toContact(person: Person) {
        navigator.toPersonDetail(person)
    }
}