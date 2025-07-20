package com.kroy.sseditor.data.remote

import com.kroy.sseditor.data.remote.models.ApiResponse
import com.kroy.sseditor.data.remote.models.TweetListItem
import com.kroy.sseditor.data.remote.models.AddClientBody
import com.kroy.sseditor.data.remote.models.AddContactBody
import com.kroy.sseditor.data.remote.models.CopyContactReqBody
import com.kroy.sseditor.data.remote.models.EditClientBody
import com.kroy.sseditor.data.remote.models.EditContactBody
import com.kroy.sseditor.data.remote.models.UserLoginBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {

    //Log In
    @POST("login")
    suspend fun loginUser(@Body driverLogsBody: UserLoginBody): Response<ApiResponse.UserLoginResponse>


    //Client
    @GET("clients")
    suspend fun getAllClients(@Query("userId") userId: Int): Response<ApiResponse.AllClientResponse>

    @POST("clients")
    suspend fun addClient(
        @Body driverLogsBody: AddClientBody,
    ): Response<ApiResponse.AddClientResponse>


    @PUT("clients/updateclient")
    suspend fun editClient(
        @Query("clientId") clientId: Int,
        @Body editClientBody: EditClientBody
    ): Response<ApiResponse.AddClientResponse>


    //Contact Screen
    @GET("contacts")
    suspend fun getAllContacts(): Response<ApiResponse.AllContactResponse>

    @POST("contacts")
    suspend fun addContact(
        @Body addContactBody: AddContactBody,
    ): Response<ApiResponse.AddContacttResponse>

    @GET("contacts/contactbyid")
    suspend fun getContactDetails(
        @Query("contactId") contactId: Int
    ): Response<ApiResponse.ContactDetailsResponse>

    @PUT("contacts/updatecontact")
    suspend fun editContact(
        @Query("contactId") contactId: Int,
        @Body editContactBody: EditContactBody
    ): Response<ApiResponse.EditContacttResponse>

    @GET("fetchrandomcontacts")
    suspend fun getRandomContacts(): Response<ApiResponse.RandomContactsResponse>


    @POST("contacts/copy")
    suspend fun copyContacts(
        @Body copyContactReqBody: CopyContactReqBody

    ): Response<ApiResponse.CopyContactsResponse>



    @GET("/v3/b/66ec5cb6ad19ca34f8a91599/?meta=false")
    suspend fun getTweets(@Header("X-JSON-Path") category: String): Response<List<TweetListItem>>

    @GET("/v3/b/66ec5cb6ad19ca34f8a91599/?meta=false")
    @Headers("X-JSON-Path:tweets..category")
    suspend fun getCategory(): Response<List<String>>














}