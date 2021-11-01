package org.ossiaustria.amigobox.ui.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.ossiaustria.amigobox.Navigator
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
            groupRepository.getAllGroups(true).collect {
                if (it.isSuccess && !it.valueOrNull().isNullOrEmpty()) {
                    val groups = it.valueOrNull()
                    groups?.firstOrNull()?.members?.let { _persons.value = it }
                } else {
                    _persons.value = emptyList()
                }
            }
        }
    }

    fun backToHome() {
        navigator.toHome()
    }

    fun toContact(person: Person) {
        navigator.toPersonDetail(person)
    }
}