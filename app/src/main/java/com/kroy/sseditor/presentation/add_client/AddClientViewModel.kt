package com.kroy.sseditor.presentation.add_client

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kroy.sseditor.data.remote.models.AddClientBody
import com.kroy.sseditor.domain.repo.ClientRepo
import com.kroy.sseditor.utils.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddClientViewModel @Inject constructor(
    private val dataStoreHelper: DataStoreHelper,
    private val clientRepo: ClientRepo,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    val userIdFlow: Flow<Int> = dataStoreHelper.userIdFlow



    fun addClient(addClientBody: AddClientBody) {
        Log.d("izaz", "Add Client Called")

        viewModelScope.launch {
            clientRepo.addClient(addClientBody)
        }
    }




}
