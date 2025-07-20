package com.kroy.sseditor.presentation.client

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kroy.sseditor.data.mapper.toClientList
import com.kroy.sseditor.data.remote.models.ApiResponse
import com.kroy.sseditor.data.remote.models.AddClientBody
import com.kroy.sseditor.domain.repo.ClientRepo
import com.kroy.sseditor.data.repo.SSEditorRepository
import com.kroy.sseditor.utils.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val dataStoreHelper: DataStoreHelper,
    private val repository: SSEditorRepository,
    private val savedStateHandle: SavedStateHandle,
    private val clientRepo: ClientRepo
) : ViewModel() {

    private val _clientScreenState = MutableStateFlow(ClientScreenState())
    val clientScreenState = _clientScreenState.asStateFlow()

    private fun setLoading(isLoading: Boolean) {
        viewModelScope.launch {
            _clientScreenState.update {
                it.copy(isLoading = isLoading)
            }
        }
    }




    init {
        viewModelScope.launch {
            val userId = savedStateHandle.get<Int>("userId") ?: 0
            Log.d("received viewmodel->", "$userId")
            setUserId(userId)
            getAllClients(userId)
        }
    }

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




    private fun getAllClients(userId: Int) {
        viewModelScope.launch {
            setLoading(true)
            clientRepo.getAllClients(userId).collect{
                val allClients = it.data?.toClientList() ?: emptyList()
                _clientScreenState.update {
                    it.copy(clients = allClients)
                }
            }
            setLoading(false)
        }
    }






    // Handle and filter the API response
    private fun handleClientResponse(response: ApiResponse) {
        when (response) {
            is ApiResponse.AllClientResponse -> {
//                if (response.data != null) {
//                    _filteredClientResponse.value = response
//                    setLoading(false)
//                } else {
//                    _filteredClientResponse.value = ApiResponse.AllClientResponse(
//                        data = emptyList(),
//                        message = response.message,
//                        statusCode = response.statusCode
//                    )
//                }
            }
            is ApiResponse.AddClientResponse -> {
//                if (response.data!=null) {
//                    // Emit the successful response
//                    _filteredaddClientResponse.value = response
//                } else {
//                    // Handle empty data scenario
//                    _filteredaddClientResponse.value = ApiResponse.AddClientResponse(
//                        data = null,
//                        message = response.message,
//                        statusCode = response.statusCode
//                    )
//                }
            }
            else -> {
                // Handle other response types if necessary
//                _filteredClientResponse.value = null
            }
        }
    }
}
