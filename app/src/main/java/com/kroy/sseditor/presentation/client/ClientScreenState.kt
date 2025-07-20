package com.kroy.sseditor.presentation.client

import com.kroy.sseditor.domain.models.Client

data class ClientScreenState(
    val userId: Int = 0,
    val isLoading: Boolean = false,
    val clients: List<Client> = emptyList()
)
