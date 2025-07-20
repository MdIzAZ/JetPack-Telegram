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


/*
val chatMessages = listOf(
    ChatMessage(
        id = 1,
        isTextMessage = true,
        text = "Hey, how are you? I am Sk Md Izaz",
        timestamp = "10:00 AM",
        isSender = true
    ),
    ChatMessage(
        id = 2,
        isTextMessage = true,
        text = "Hey, how are you?",
        timestamp = "10:00 AM",
        isSender = true
    ),
//    ChatMessage(
//        id = 3,
//        isTextMessage = false,
//        text = "",
//        nonTextMessage = NonTextMessage.Sticker(R.drawable.dummy_sticker),
//        timestamp = "10:01 AM",
//        isSender = false
//    ),
    ChatMessage(
        id = 4,
        isTextMessage = true,
        text = "The overlapping/darker portion you're seeing is because the triangle tail is drawn on top of the bubble and both use semi-transparent black colors (Color.Black.copy(alpha = 0.7f) for the bubble and Color(0x80000000) for the tail). Since alpha blending stacks, this overlap results in a visibly darker region.",
        timestamp = "10:02 AM",
        isSender = false
    ),
//    ChatMessage(
//        id = 5,
//        isTextMessage = false,
//        text = "",
//        nonTextMessage = NonTextMessage.Image(R.drawable.f),
//        timestamp = "10:03 AM",
//        isSender = true
//    ),
    ChatMessage(
        id = 6,
        isTextMessage = true,
        text = "Wow! That looks amazing!",
        timestamp = "10:04 AM",
        isSender = false
    ),
//    ChatMessage(
//        id = 7,
//        isTextMessage = false,
//        text = "",
//        nonTextMessage = NonTextMessage.Sticker(R.drawable.dummy_sticker),
//        timestamp = "10:05 AM",
//        isSender = true
//    ),
    ChatMessage(
        id = 8,
        isTextMessage = true,
        text = "Here's another one",
        timestamp = "10:06 AM",
        isSender = false
    ),
//    ChatMessage(
//        id = 9,
//        isTextMessage = false,
//        text = "",
//        nonTextMessage = NonTextMessage.Image(R.drawable.d),
//        timestamp = "10:07 AM",
//        isSender = false
//    ),
    ChatMessage(
        id = 10,
        isTextMessage = true,
        text = "If the screen still jumps or flickers when you close the notification " +
                "panel or return to the activity, despite implementing hideSystemUI() and " +
                "reapplying it in onWindowFocusChanged(), " +
                "then the issue likely lies in how Jetpack Compose and system window insets are interacting",
        timestamp = "10:08 AM",
        isSender = true
    ),
    ChatMessage(
        id = 11,
        isTextMessage = true,
        text = "Sure thing 😄",
        timestamp = "10:09 AM",
        isSender = false
    )
)
*/