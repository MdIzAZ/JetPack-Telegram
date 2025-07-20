package com.kroy.sseditor.data.remote.models


data class ContactDetails(
    val contactId:Int = 0,
    val contactImage:String = "",
    val contactName:String="",
    val comment1:String="",
    val comment2:String="",
    val comment3:String="",
    val uploadedImage:String="",
)
