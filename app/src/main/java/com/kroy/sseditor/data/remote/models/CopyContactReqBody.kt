package com.kroy.sseditor.data.remote.models

data class CopyContactReqBody(
    val clientId: Int,
    val contactList: List<ContactResponse>,
    val dayName: String
)