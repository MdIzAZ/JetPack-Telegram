package com.kroy.sseditor.data.remote.temp

import kotlinx.serialization.Serializable

@Serializable
data class ContactDto(
    val id: Int,
    val name: Name,
    val photo: Photo,
    val comments: List<MessageDto>,
)

//@Serializable
//data class MessageDto(
//    val id: Int,
//    val isImage: Boolean,
//    val text: String,
//    val url: String?
//)

@Serializable
data class MessageDto(
    val comment: String,
    val id: Int,
    val isImage: Int
)


@Serializable
data class Name(
    val id: Int,
    val value: String
)


@Serializable
data class Photo(
    val id: Int,
    val url: String?
)