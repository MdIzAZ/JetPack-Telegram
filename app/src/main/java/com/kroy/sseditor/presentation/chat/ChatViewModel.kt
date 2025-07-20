package com.kroy.sseditor.presentation.chat

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {


    val chatName: String? = savedStateHandle.get<String>("name")


    private val _state = MutableStateFlow(ChatScreenState())
    val state = _state.asStateFlow()




}