package com.kroy.sseditor.data.repo

import android.util.Log
import com.kroy.sseditor.data.remote.ApiService
import com.kroy.sseditor.data.remote.models.ApiResponse
import com.kroy.sseditor.domain.repo.ContactRepo
import com.kroy.sseditor.utils.SelectedClient.clientId
import com.kroy.sseditor.utils.SelectedClient.dayName
import javax.inject.Inject

class ContactRepoImp @Inject constructor(
    private val apiService: ApiService
) : ContactRepo {

    override suspend fun getRandomContacts(): ApiResponse.RandomContactsResponse {

        return try {
            val response = apiService.getRandomContacts()
            Log.d("izaz", "${response.body()}")
            
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                ApiResponse.RandomContactsResponse(
                    data = emptyList(),
                    message = response.message() ?: "Unknown error",
                    statusCode = response.code()
                )
            }
        } catch (e: Exception) {
            Log.d("izaz", e.message ?: "Unknown error")
            ApiResponse.RandomContactsResponse(
                data = emptyList(),
                message = e.message ?: "Unknown error",
                statusCode = -1
            )
        }


    }
}