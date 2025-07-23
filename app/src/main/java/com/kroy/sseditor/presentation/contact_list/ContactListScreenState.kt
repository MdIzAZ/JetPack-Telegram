package com.kroy.sseditor.presentation.contact_list

import com.kroy.ssediotor.R
import com.kroy.sseditor.domain.models.ContactItem
import com.kroy.sseditor.domain.models.OSType

data class ContactListScreenState(
    val currentOsType: OSType = OSType.Android,
    val isListUpdatingStarted: Boolean = false,
    val totalUnreadMessages: Int = 0,
    val notificationBarTime: String = "12:00 PM",
    val battery: Pair<Int, Int> = Pair(R.drawable.battery70, 70),
    val contactItems: List<ContactItem> = emptyList(),
    val folders: List<Pair<String, Int>> = defaultFolderList
)


val defaultFolderList = listOf(
    Pair("All", 12),
    Pair("Members", 5),
    Pair("Unread", 43),
    Pair("Channel", 9),
)


