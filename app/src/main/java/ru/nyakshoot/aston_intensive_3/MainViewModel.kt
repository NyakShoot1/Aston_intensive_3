package ru.nyakshoot.aston_intensive_3

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.nyakshoot.aston_intensive_3.recycler.ContactModel

class MainViewModel : ViewModel() {

    private val _currentContacts: MutableLiveData<MutableList<ContactModel>> = MutableLiveData()
    private val _deletionState = MutableLiveData(false)
    private val _selectedContacts: MutableLiveData<MutableSet<ContactModel>> = MutableLiveData()

    init {
        _currentContacts.value = MutableList(30) {
            ContactModel(it, "Name$it", "Surname$it", "phone$it")
        }
        _selectedContacts.value = mutableSetOf()
    }

    fun getCurrentContacts() = _currentContacts
    fun getDeletionState() = _deletionState
    fun getSelectedContacts() = _selectedContacts

    fun addNewContact(contact: ContactModel) {
        val updatedContacts = _currentContacts.value ?: mutableListOf()
        updatedContacts.add(contact)
        _currentContacts.value = updatedContacts
    }


    fun selectContactForDeletion(contact: ContactModel) {
        if (_selectedContacts.value?.contains(contact) == true)
            _selectedContacts.value?.remove(contact)
        else
            _selectedContacts.value?.add(contact)
        Log.i("test", getSelectedContacts().value.toString())
    }

    fun updateDeletionState() {
        _deletionState.value = !_deletionState.value!!
        if (!_deletionState.value!!) {
            _selectedContacts.value?.clear()
        }
    }

    fun deleteSelectedContacts() {
        val updatedContacts = _currentContacts.value?.toMutableList()
        updatedContacts?.removeAll(_selectedContacts.value ?: emptySet())
        _currentContacts.value = updatedContacts
        _selectedContacts.value?.clear()
    }


    fun editContact(position: Int, contact: ContactModel) {
        val updatedContacts = _currentContacts.value ?: mutableListOf()
        updatedContacts[position] = contact
        _currentContacts.value = updatedContacts
    }

    fun moveContact(fromPosition: Int, toPosition: Int) {
        val updatedContacts = _currentContacts.value ?: return
        updatedContacts.removeAt(fromPosition).also { item ->
            updatedContacts.add(toPosition, item)
        }
        _currentContacts.value = updatedContacts
    }
}
