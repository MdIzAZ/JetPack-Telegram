package com.kroy.sseditor.data.repo

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.kroy.sseditor.data.remote.ApiService
import com.kroy.sseditor.data.remote.models.AddContactBody
import com.kroy.sseditor.data.remote.models.ApiResponse
import com.kroy.sseditor.data.remote.models.CopyContactReqBody
import com.kroy.sseditor.data.remote.models.EditClientBody
import com.kroy.sseditor.data.remote.models.EditContactBody
import com.kroy.sseditor.data.remote.models.TweetListItem
import com.kroy.sseditor.data.remote.models.UserLoginBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SSEditorRepository @Inject constructor(private val apiService: ApiService) {


    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>>
        get() = _categories

    private val _tweets = MutableStateFlow<List<TweetListItem>>(emptyList())
    val tweets: StateFlow<List<TweetListItem>>
        get() = _tweets


    private val _userlogin = MutableStateFlow(ApiResponse.UserLoginResponse())

    val userlogin: StateFlow<ApiResponse>
        get() = _userlogin

    suspend fun getCategory() {
        val response = apiService.getCategory()
        if (response.isSuccessful && response.body() != null) {
            // get the categories
            _categories.emit(response.body()!!)

        }
    }

    suspend fun getTweets(category: String) {
        val response = apiService.getTweets("tweets[?(@.category==\"$category\")]")
        if (response.isSuccessful && response.body() != null) {
            // get the categories
            _tweets.emit(response.body()!!)

        }
    }

    suspend fun loginuser(userloginBody: UserLoginBody, context: Context) {
        try {
            Log.d("userlogin->", "body = $userloginBody")
            val response = apiService.loginUser(userloginBody)
            Log.d("userlogin->", "response = ${response.body()}")


            if (response.isSuccessful && response.body() != null) {
                // get the categories
                _userlogin.emit(response.body()!!)

            } else {
                // Show success toast
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


    private val _addClient =
        MutableStateFlow<ApiResponse.AddClientResponse>(ApiResponse.AddClientResponse())


    private val _editClient =
        MutableStateFlow<ApiResponse.AddClientResponse>(ApiResponse.AddClientResponse())
    val editClients: StateFlow<ApiResponse>
        get() = _addClient

    suspend fun editClient(context: Context, clientId: Int, editClientBody: EditClientBody) {
        try {
            Log.d("edit users->", "body = $editClientBody")
            val response = apiService.editClient(clientId = clientId, editClientBody)
            Log.d("edit users->", "response = ${response.body()}")

            if (response.isSuccessful && response.body() != null) {
                // get the categories
                _editClient.emit(response.body()!!)

            } else {
                // Show success toast
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private val _allContacts =
        MutableStateFlow<ApiResponse.AllContactResponse>(ApiResponse.AllContactResponse())
    val allContacts: StateFlow<ApiResponse>
        get() = _allContacts

    suspend fun getAllContacts(clientId: Int, dayName: String) {
        try {

            Log.d("all contacts->", "body = $clientId , $dayName")
            val response = apiService.getAllContacts()
            Log.d("all contacts->", "response = ${response.body()}")

            if (response.isSuccessful && response.body() != null) {
                // get the categories
                _allContacts.emit(response.body()!!)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


    //Adding ocntact
    private val _addContact =
        MutableStateFlow<ApiResponse.AddContacttResponse>(ApiResponse.AddContacttResponse())
    val addContact: StateFlow<ApiResponse>
        get() = _addContact

    suspend fun addContact(addContactBody: AddContactBody, context: Context) {
        try {
            Log.d("add contact->", "body = ${addContactBody.contactName}")
            val response = apiService.addContact(addContactBody)
            Log.d("add contact->", "response = $response")

            if (response.isSuccessful && response.body() != null) {
                Log.d("add contact->", "entered  successful = ${response.body()} ")
                // get the categories
                _addContact.emit(response.body()!!)

            } else {
                // Show success toast
                Log.d("add contact->", "entered not successful")

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    //get contact details
    private val _getContactDetails = MutableStateFlow<ApiResponse.ContactDetailsResponse>(
        ApiResponse.ContactDetailsResponse()
    )
    val getContactDetails: StateFlow<ApiResponse>
        get() = _getContactDetails

    suspend fun getContactDetails(contactId: Int, context: Context) {
        try {
            Log.d("contact details->", "body = $contactId")
            val response = apiService.getContactDetails(contactId)
            Log.d("contact details->", "response = ${response.body()}")

            if (response.isSuccessful && response.body() != null) {
                // get the categories
                _getContactDetails.emit(response.body()!!)

            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //edit contact details
    private val _editContactDetails =
        MutableStateFlow<ApiResponse.EditContacttResponse>(ApiResponse.EditContacttResponse())
    val editContactDetails: StateFlow<ApiResponse>
        get() = _editContactDetails

    suspend fun editContactDetails(
        contactId: Int,
        editContactBody: EditContactBody,
        context: Context
    ) {
        try {
            Log.d(" edit contact details->", "body = contactID $contactId , $editContactBody")
            val response = apiService.editContact(
                contactId = contactId,
                editContactBody = editContactBody
            )
            Log.d(" edit contact details->", "response = ${response.body()?.data}")

            if (response.isSuccessful && response.body() != null) {
                // get the categories
                _editContactDetails.emit(response.body()!!)

            } else {
                // Show success toast
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    //random contact details
    private val _randomContacts =
        MutableStateFlow<ApiResponse.RandomContactsResponse>(ApiResponse.RandomContactsResponse())
    val randomContactsResponse: StateFlow<ApiResponse>
        get() = _randomContacts

    suspend fun getRandomContacts(clientId: Int, dayName: String, context: Context) {
        try {
            Log.d("get random contacts->", "body = client Id $clientId , $dayName")
            val response = apiService.getRandomContacts()
            Log.d("get random contacts->", "response = ${response.body()}")

            if (response.isSuccessful && response.body()?.data != null) {
                // get the categories
                _randomContacts.emit(response.body()!!)

            } else {
                // Show success toast
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    //copy contacts
    private val _copyContacts =
        MutableStateFlow<ApiResponse.CopyContactsResponse>(ApiResponse.CopyContactsResponse())
    val copyContactsResponse: StateFlow<ApiResponse>
        get() = _copyContacts

    suspend fun copyContacts(copyContactReqBody: CopyContactReqBody, context: Context) {
        try {
            Log.d("copy contacts->", "body = $copyContactReqBody")
            val response = apiService.copyContacts(copyContactReqBody)
            Log.d("copy contacts->", "response = ${response.body()}")

            if (response.isSuccessful && response.body()?.data != null) {
                // get the categories
                _copyContacts.emit(response.body()!!)

            } else {
                // Show success toast
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}