package com.kroy.sseditor.presentation.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kroy.sseditor.data.remote.models.ApiResponse
import com.kroy.sseditor.data.remote.models.UserLoginBody
import com.kroy.sseditor.data.repo.SSEditorRepository
import com.kroy.sseditor.utils.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    private val dataStoreHelper: DataStoreHelper,
    private val repository: SSEditorRepository
) : ViewModel() {

    // Expose the DataStore data as Flow
    val isLoggedInFlow: Flow<Boolean> = dataStoreHelper.isLoggedInFlow
    val userNameFlow: Flow<Int> = dataStoreHelper.userIdFlow

    // Functions to update the values
    fun setIsLoggedIn(isLoggedIn: Boolean) {
        viewModelScope.launch {
            dataStoreHelper.setIsLoggedIn(isLoggedIn)
        }
    }

    fun setUserName(userId: Int) {
        viewModelScope.launch {
            dataStoreHelper.setUserId(userId)
        }
    }

    val userlogin: StateFlow<ApiResponse>
        get() = repository.userlogin

    // Filtered response
    private val _filteredUserLoginResponse = MutableStateFlow<ApiResponse.UserLoginResponse?>(null)
    val filteredUserLoginResponse: StateFlow<ApiResponse.UserLoginResponse?> get() = _filteredUserLoginResponse

    init {
        // Collect and filter the API response
        viewModelScope.launch {
            userlogin.collect { response ->
                filterApiResponse(response)
            }
        }
    }

    fun loginuser(userloginBody: UserLoginBody, context: Context) {
        viewModelScope.launch {
            repository.loginuser(userloginBody,context)
        }
    }

    private fun filterApiResponse(response: ApiResponse) {
        when (response) {
            is ApiResponse.UserLoginResponse -> {
                // Check if the login was successful
                if (response.data!=null) {
                    // Emit the successful login response
                    _filteredUserLoginResponse.value = response
                    setUserName(response.data?.userId ?: 0) // Update user ID if available
                } else {
                    // Emit an error response or handle accordingly
                    _filteredUserLoginResponse.value = ApiResponse.UserLoginResponse(message = response.message, statusCode = response.statusCode)
                }
            }
            else -> {
                // Handle other response types if necessary
                _filteredUserLoginResponse.value = null
            }
        }
    }
}
