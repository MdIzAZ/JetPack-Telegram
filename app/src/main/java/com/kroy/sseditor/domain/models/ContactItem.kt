package com.kroy.sseditor.domain.models

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color

data class ContactItem(
    val id: Int,
    val name: String,
    val color: Color? = null,
    val profileImage: Bitmap? = null,
    val uiTime: String = "12:00 AM",
    val messages: List<ChatMessage>,
    val unreadCount: Int? = 0,
)

val dummyContacts = listOf(
    ContactItem(
        id = 1,
        name = "Alice",
        color = Color(0xFFE57373),
        uiTime = "09:15 AM",
        messages = dummyChatMessages,
        unreadCount = 2
    ),
    ContactItem(
        id = 2,
        name = "Bob",
        color = Color(0xFF64B5F6),
        uiTime = "08:30 AM",
        messages = dummyChatMessages,
        unreadCount = 0
    ),
    ContactItem(
        id = 3,
        name = "Charlie",
        color = Color(0xFF81C784),
        uiTime = "Yesterday",
        messages = dummyChatMessages,
        unreadCount = 1
    ),
    ContactItem(
        id = 4,
        name = "Diana",
        color = Color(0xFFFFB74D),
        uiTime = "11:59 PM",
        messages = dummyChatMessages,
        unreadCount = 3
    ),
    ContactItem(
        id = 5,
        name = "Ethan",
        color = Color(0xFFBA68C8),
        uiTime = "12:45 PM",
        messages = dummyChatMessages,
        unreadCount = 0
    ),
    ContactItem(
        id = 6,
        name = "Fiona",
        color = Color(0xFFA1887F),
        uiTime = "06:30 AM",
        messages = dummyChatMessages,
        unreadCount = 1
    ),
    ContactItem(
        id = 7,
        name = "George",
        color = Color(0xFF4DB6AC),
        uiTime = "10:00 AM",
        messages = dummyChatMessages,
        unreadCount = 0
    ),
    ContactItem(
        id = 8,
        name = "Hannah",
        color = Color(0xFFFF8A65),
        uiTime = "Yesterday",
        messages = dummyChatMessages,
        unreadCount = 2
    ),
    ContactItem(
        id = 9,
        name = "Ian",
        color = Color(0xFF9575CD),
        uiTime = "03:00 PM",
        messages = dummyChatMessages,
        unreadCount = 0
    ),
    ContactItem(
        id = 10,
        name = "Jenna",
        color = Color(0xFF90A4AE),
        uiTime = "Yesterday",
        messages = dummyChatMessages,
        unreadCount = 1
    ),
    ContactItem(
        id = 11,
        name = "Kevin",
        color = Color(0xFFF06292),
        uiTime = "07:15 AM",
        messages = dummyChatMessages,
        unreadCount = 0
    ),
    ContactItem(
        id = 12,
        name = "Lily",
        color = Color(0xFF4FC3F7),
        uiTime = "Today",
        messages = dummyChatMessages,
        unreadCount = 4
    )
)
