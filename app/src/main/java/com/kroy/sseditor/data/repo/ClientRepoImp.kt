package com.kroy.sseditor.data.repo

import android.util.Log
import com.kroy.sseditor.data.remote.ApiService
import com.kroy.sseditor.data.remote.models.AddClientBody
import com.kroy.sseditor.data.remote.models.ApiResponse
import com.kroy.sseditor.domain.repo.ClientRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ClientRepoImp @Inject constructor(
    private val apiService: ApiService
) : ClientRepo {

    override fun getAllClients(userId: Int): Flow<ApiResponse.AllClientResponse> {
        return flow {
            try {
                Log.d("izaz", "UserId: $userId")

                val response = apiService.getAllClients(userId)
//                Log.d("izaz", "Clients:${response.body()}")
                response.body()?.data?.forEach {
                    Log.d("izaz", "id:${it.clientId} name: ${it.clientName} Img: ${it.clientImage.length}")
                }

                if (response.isSuccessful && response.body() != null) {
                    emit(response.body()!!)
                } else {
                    emit(
                        ApiResponse.AllClientResponse(
                            data = emptyList(),
                            message = response.message() ?: "Unknown error",
                            statusCode = response.code()
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(
                    ApiResponse.AllClientResponse(
                        data = emptyList(),
                        message = e.localizedMessage ?: "Exception occurred",
                        statusCode = -1
                    )
                )
            }
        }
    }

    override suspend fun addClient(
        addClientBody: AddClientBody
    ): ApiResponse.AddClientResponse {

        return try {

            val response = apiService.addClient(addClientBody)

            if (response.isSuccessful && response.body() != null) {
                Log.d("hanif", "Add client response success")
                Log.d("hanif", "Response body: ${response.body()}")

                return response.body()!!
            } else {
                Log.e("izaz", "Add client response fail")
                return ApiResponse.AddClientResponse(
                    data = null,
                    message = response.message() ?: "Unknown error",
                    statusCode = response.code()
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
            ApiResponse.AddClientResponse(
                data = null,
                message = e.localizedMessage ?: "Exception occurred",
                statusCode = -1
            )
        }

    }


}