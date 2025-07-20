package com.kroy.sseditor.data.remote.models

data class AddClientBody(
    val clientImage: String,
    val clientName: String,
    val userId: Int,
    val backgroundImage: String,
)