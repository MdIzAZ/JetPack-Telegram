package com.kroy.sseditor.data.remote.models

import com.kroy.sseditor.data.remote.temp.ContactDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


sealed class ApiResponse {

    data class UserLoginResponse(
        val data: UserData? = null,
        val message: String = "",
        val statusCode: Int = 0
    ) : ApiResponse()

    data class AddClientResponse(
        val data: AddUserData? = null,
        val message: String = "",
        val statusCode: Int = 0
    ) : ApiResponse()

    data class AllClientResponse(
        val data: List<ClientResponse>? = emptyList(),
        val message: String = "",
        val statusCode: Int = 0
    ) : ApiResponse()


    @Serializable
    data class AllContactResponse(
        val data: List<ContactDto>? = emptyList(),
        val message: String = "",
        @SerialName("status_code")
        val statusCode: Int = 0
    ) : ApiResponse()

    data class AddContacttResponse(
        val data: AddContactResponse? = null,
        val message: String = "",
        val statusCode: Int = 0
    ) : ApiResponse()

   data class EditContacttResponse(
       val data: AddContactResponse? = null,
       val message: String = "",
       val statusCode: Int = 0
    ) : ApiResponse()

    data class ContactDetailsResponse(
        val data: ContactDetails? = null,
        val message: String = "",
        val statusCode: Int = 0
    ) : ApiResponse()

    data class RandomContactsResponse(
        val data: List<ContactDto>? = emptyList(),
        val message: String = "",
        @SerialName("status_code")
        val statusCode: Int = 0
    ) : ApiResponse()

    data class CopyContactsResponse(
        val data: CopyContactData? = null,
        val message: String = "",
        val statusCode: Int = 0
    ) : ApiResponse()
}


data class UserData(
    val token: String = "",
    val userId: Int = 0
)
data class AddUserData(
    val clientId: Int = 0
)
data class CopyContactData(
    val totalTimeTaken: String = "",
    val duplicateContact : ContactResponse? = null
)

// Example client item data class

