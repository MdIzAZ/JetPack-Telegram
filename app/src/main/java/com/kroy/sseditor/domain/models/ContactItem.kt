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