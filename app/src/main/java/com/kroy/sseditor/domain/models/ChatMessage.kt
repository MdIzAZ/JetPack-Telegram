package com.kroy.sseditor.domain.models

data class ChatMessage(
    val id: Int,
    val recipientId: Int,
    val isTextMessage: Boolean,
    val text: String,
    val nonTextMessage: NonTextMessage? = null,
    val timestamp: String = "12:00 AM",
    val isSender: Boolean
)


val dummyChatMessages = listOf(
    ChatMessage(1, 1, true, "Hey!", null, "09:00 AM", true),
    ChatMessage(2, 1, true, "How are you?", null, "09:02 AM", false),
    ChatMessage(3, 2, true, "Let's meet today", null, "10:15 AM", true),
    ChatMessage(4, 2, true, "Sure, what time?", null, "10:17 AM", false),
    ChatMessage(5, 3, true, "Did you finish the report?", null, "11:00 AM", true),
    ChatMessage(6, 3, true, "Yes, sent it via email.", null, "11:05 AM", false),
    ChatMessage(7, 4, true, "Happy Birthday!", null, "12:00 PM", true),
    ChatMessage(8, 4, true, "Thank you!", null, "12:01 PM", false),
    ChatMessage(9, 5, true, "Where are you?", null, "01:30 PM", true),
    ChatMessage(10, 5, true, "Coming in 5 minutes. Hello guys i am in the i am in the ", null, "01:32 PM", false)
)
