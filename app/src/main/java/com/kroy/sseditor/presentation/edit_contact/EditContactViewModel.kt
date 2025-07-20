package com.kroy.sseditor.presentation.edit_contact

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kroy.sseditor.data.remote.models.ApiResponse
import com.kroy.sseditor.data.remote.models.EditContactBody
import com.kroy.sseditor.data.repo.SSEditorRepository
import com.kroy.sseditor.utils.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditContactViewModel @Inject constructor(
    private val dataStoreHelper: DataStoreHelper,
    private val repository: SSEditorRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Expose the DataStore data as Flow
    val isLoggedInFlow: Flow<Boolean> = dataStoreHelper.isLoggedInFlow
    val userIdFlow: Flow<Int> = dataStoreHelper.userIdFlow

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Function to toggle the loading state
    fun toggleLoading() {
        viewModelScope.launch {
            _isLoading.value = !_isLoading.value
        }
    }

    // Function to explicitly set the loading state
    fun setLoading(isLoading: Boolean) {
        viewModelScope.launch {
            _isLoading.value = isLoading
        }
    }


    // StateFlow for all clients response


//    init {
//
//        viewModelScope.launch {
//            getcontactDetails(SelectedContact.contactId, context =a )
//        }
//    }

    // Functions to update user state
    private fun setIsLoggedIn(isLoggedIn: Boolean) {
        viewModelScope.launch {
            dataStoreHelper.setIsLoggedIn(isLoggedIn)
        }
    }

    private fun setUserId(userId: Int) {
        viewModelScope.launch {
            dataStoreHelper.setUserId(userId)
        }
    }

    // Fetch all clients and handle the response
    val allContacts: StateFlow<ApiResponse> get() = repository.allContacts

    // Filtered response for all clients
    private val _filteredContactResponse = MutableStateFlow<ApiResponse.AllContactResponse?>(null)
    val filteredContactResponse: StateFlow<ApiResponse.AllContactResponse?> get() = _filteredContactResponse
    fun getAllContacts(clientId:Int,dayName:String) {
        viewModelScope.launch {
            Log.d("ContactViewModel", "Fetching all clients for userId: $clientId")
            repository.getAllContacts(clientId, dayName)

            // Assuming repository.allClients is updated after the API call
            allContacts.collect { response ->
                handleClientResponse(response)
            }
        }
    }




    private val editContact: StateFlow<ApiResponse> get() = repository.editContactDetails

    // Filtered response for all clients
    private val _filterededitContactResponse = MutableStateFlow<ApiResponse.EditContacttResponse?>(null)
    val filterededitContactResponse: StateFlow<ApiResponse.EditContacttResponse?> get() = _filterededitContactResponse
    fun editContact(contactId:Int, editContactBody: EditContactBody, context:Context) {
        viewModelScope.launch {

            repository.editContactDetails(contactId = contactId,
                editContactBody = editContactBody,
                context
            )

            // Assuming repository.allClients is updated after the API call
            editContact.collect { response ->
                handleClientResponse(response)
            }
        }
    }

    // Add contact and handle the response
    private val getContactDetails: StateFlow<ApiResponse> get() = repository.getContactDetails

    // Filtered response for all clients
    private val _filteredgetContactDetailsResponse = MutableStateFlow<ApiResponse.ContactDetailsResponse?>(null)
    val filteredgetContactDetailsResponse: StateFlow<ApiResponse.ContactDetailsResponse?> get() = _filteredgetContactDetailsResponse
    fun getcontactDetails(contactId:Int, context:Context) {
        viewModelScope.launch {

            repository.getContactDetails(contactId,context)

            // Assuming repository.allClients is updated after the API call
            getContactDetails.collect { response ->
                handleClientResponse(response)
            }
        }
    }







    // Handle and filter the API response
    private fun handleClientResponse(response: ApiResponse) {
        when (response) {
            is ApiResponse.AllContactResponse -> {
                if (response.data!!.isNotEmpty()) {
                    // Emit the successful response
                    _filteredContactResponse.value = response
                } else {
                    // Handle empty data scenario
                    _filteredContactResponse.value = ApiResponse.AllContactResponse(
                        data = emptyList(),
                        message =response.message,
                        statusCode = response.statusCode
                    )
                }
            }


            is ApiResponse.ContactDetailsResponse -> {
                if (response.data!=null) {
                    // Emit the successful response
                    _filteredgetContactDetailsResponse.value = response
                } else {
                    // Handle empty data scenario
                    _filteredgetContactDetailsResponse.value = ApiResponse.ContactDetailsResponse(
                        data = null,
                        message = response.message,
                        statusCode = response.statusCode
                    )
                }
            }

            is ApiResponse.EditContacttResponse -> {
                if (response.data!=null) {
                    // Emit the successful response
                    _filterededitContactResponse.value = response
                } else {
                    // Handle empty data scenario
                    _filterededitContactResponse.value = ApiResponse.EditContacttResponse(
                        data = null,
                        message = response.message,
                        statusCode = response.statusCode
                    )
                }
            }
            else -> {
                // Handle other response types if necessary
                _filteredContactResponse.value = null
            }
        }
    }

    fun resetContactState() {
//        _filteredaddContactResponse.value = null
//        _filteredContactResponse.value = null
        _filteredgetContactDetailsResponse.value= null
    }

}
