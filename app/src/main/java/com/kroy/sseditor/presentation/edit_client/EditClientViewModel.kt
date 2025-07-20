package com.kroy.sseditor.presentation.edit_client

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kroy.sseditor.data.remote.models.ApiResponse
import com.kroy.sseditor.data.remote.models.EditClientBody
import com.kroy.sseditor.data.repo.SSEditorRepository
import com.kroy.sseditor.utils.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditClientViewModel @Inject constructor(
    private val dataStoreHelper: DataStoreHelper,
    private val repository: SSEditorRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Expose the DataStore data as Flow
    val isLoggedInFlow: Flow<Boolean> = dataStoreHelper.isLoggedInFlow
    val userIdFlow: Flow<Int> = dataStoreHelper.userIdFlow

    // StateFlow for all clients response


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
    private val editClients: StateFlow<ApiResponse>
        get() = repository.editClients

    // Filtered response for all clients
    private val _filteredEditClientResponse = MutableStateFlow<ApiResponse.AddClientResponse?>(null)
    val filteredEditClientResponse: StateFlow<ApiResponse.AddClientResponse?> get() = _filteredEditClientResponse


    fun editClient(editClientBody: EditClientBody, clientId:Int, context: Context) {
        viewModelScope.launch {

            repository.editClient(context = context,clientId=clientId,editClientBody)

            // Assuming repository.allClients is updated after the API call
            editClients.collect { response ->

                handleClientResponse(response)
            }
        }
    }




    // Handle and filter the API response
    private fun handleClientResponse(response: ApiResponse) {
        when (response) {
            is ApiResponse.AddClientResponse -> {
                if (response.data!=null) {
                    // Emit the successful response
                    _filteredEditClientResponse.value = response
                } else {
                    // Handle empty data scenario
                    _filteredEditClientResponse.value = ApiResponse.AddClientResponse(
                        data = null,
                        message = response.message,
                        statusCode = response.statusCode
                    )

                }
            }
            else -> {
            }
        }
    }

    fun resetClientState() {
        _filteredEditClientResponse.value = null
    }

}
