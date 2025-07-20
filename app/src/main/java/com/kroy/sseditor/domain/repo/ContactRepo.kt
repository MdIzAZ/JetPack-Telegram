package com.kroy.sseditor.domain.repo

import com.kroy.sseditor.data.remote.models.ApiResponse

interface ContactRepo {

    suspend fun getRandomContacts(): ApiResponse.RandomContactsResponse

}