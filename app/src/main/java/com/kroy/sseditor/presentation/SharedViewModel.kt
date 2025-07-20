package com.kroy.sseditor.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kroy.sseditor.domain.models.ThemeMode
import com.kroy.sseditor.domain.repo.SettingsRepo
import com.kroy.sseditor.data.remote.models.ContactResponse
import com.kroy.sseditor.domain.models.Client
import com.kroy.sseditor.domain.models.OSType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val settingsRepo: SettingsRepo
) : ViewModel() {


    val currentTheme = settingsRepo.getCurrentTheme()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeMode.SYSTEM
        )

    val currentOSType = settingsRepo.getCurrentOsType()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = OSType.Android
        )


    private val _clients = MutableLiveData<List<Client>>()
    val clients: LiveData<List<Client>> get() = _clients

    private val _contacts = MutableLiveData<List<ContactResponse>>()
    val contacts: LiveData<List<ContactResponse>> get() = _contacts


    fun setContacts(contactList: List<ContactResponse>) {
        _contacts.value = contactList
    }

    fun setClients(clientList: List<Client>) {
        _clients.value = clientList
    }

    fun saveThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            try {
                settingsRepo.saveThemePref(themeMode)
            } catch (e: Exception) {
                Log.d("izaz", e.message ?: "Unknown error")
            }
        }

    }


    fun changeOsType(os: OSType) {
        viewModelScope.launch {
            try {
                settingsRepo.saveOsTypePref(os)
            } catch (e: Exception) {
                Log.d("izaz", e.message ?: "Unknown error")
            }
        }

    }

}
