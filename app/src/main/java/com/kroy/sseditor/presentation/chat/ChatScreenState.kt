package com.kroy.sseditor.presentation.chat

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import com.kroy.ssediotor.R
import com.kroy.sseditor.domain.models.ChatMessage

data class ChatScreenState(
    val contactId: Int = 0,
    val notificationBarTime: String = "08:45 AM",
    val lastMessageTime: String = "08:45 AM",
    val timeRemaining:Int=0,
    val numberOfUnseenMessages: Int = 0,
    val contactName: String = "",
    val contactPic: Bitmap? = null,
    val battery: Pair<Int, Int> = Pair(R.drawable.battery70, 70),
    val backgroundColor: Color? = null,
    val backgroundImage: Bitmap? = null,
    val messages: List<ChatMessage> = emptyList()
)