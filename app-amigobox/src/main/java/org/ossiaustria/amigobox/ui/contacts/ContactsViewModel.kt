package org.ossiaustria.amigobox.ui.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.ossiaustria.lib.domain.models.Person
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

import org.ossiaustria.lib.domain.repositories.GroupRepository


class ContactsViewModel(
    private val groupRepository: GroupRepository
) : ViewModel() {

    private val _persons: MutableLiveData<List<Person>> = MutableLiveData()
    val persons: LiveData<List<Person>> = _persons

    fun load() {
        viewModelScope.launch {
            val first = groupRepository.getAllGroups().first()
            if (first.isSuccess) {
                val groups = first.valueOrNull()
                groups?.flatMap { it.members }?.let {
                    _persons.value = it
                }
            }
        }
    }

}