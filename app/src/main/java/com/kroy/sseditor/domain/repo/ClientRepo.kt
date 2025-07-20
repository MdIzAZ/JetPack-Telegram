package com.kroy.sseditor.domain.repo

import com.kroy.sseditor.data.remote.models.AddClientBody
import com.kroy.sseditor.data.remote.models.ApiResponse
import kotlinx.coroutines.flow.Flow

interface ClientRepo {

    fun getAllClients(userId: Int): Flow<ApiResponse.AllClientResponse>

    suspend fun addClient(addClientBody: AddClientBody): ApiResponse.AddClientResponse

}