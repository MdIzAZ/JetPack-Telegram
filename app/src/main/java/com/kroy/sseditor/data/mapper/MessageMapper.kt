package com.kroy.sseditor.data.mapper

import com.kroy.sseditor.data.remote.temp.MessageDto
import com.kroy.sseditor.domain.models.ChatMessage
import com.kroy.sseditor.domain.models.NonTextMessage
import com.kroy.sseditor.utils.Utils

fun MessageDto.toChatMessages(contactId: Int): ChatMessage {
    return ChatMessage(
        id = this.id,
        recipientId = contactId,
        isSender = false,
        isTextMessage = this.isImage == 0,
        text = if (this.isImage == 0) this.comment else "",
        nonTextMessage = Utils.base64ToBitmap(this.comment.substringAfter("base64,"))
            ?.let {
                when(this.isImage) {
                    1 -> NonTextMessage.Image(it)
                    2->NonTextMessage.Sticker(it)
                    else -> null
                }
            }
    )
}


fun List<MessageDto>.toChatMessageList(contactId: Int): List<ChatMessage> {
    return this.map {
        it.toChatMessages(contactId)
    }
}
