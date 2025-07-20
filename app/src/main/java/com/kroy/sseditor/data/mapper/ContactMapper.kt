package com.kroy.sseditor.data.mapper

import com.kroy.sseditor.data.remote.temp.ContactDto
import com.kroy.sseditor.domain.models.ContactItem
import com.kroy.sseditor.presentation.theme.RandomBgColorPairs
import com.kroy.sseditor.utils.Utils

fun ContactDto.toContactItem(): ContactItem {
    return ContactItem(
        id = this.id,
        name = this.name.value,
        color = RandomBgColorPairs.random().first,
        messages = this.comments.toChatMessageList(this.id),
        profileImage = Utils.base64ToBitmap(this.photo.url?.substringAfter("base64,")),
        unreadCount = this.comments.size  ?: 0
    )
}


fun List<ContactDto>.toContactItemList(): List<ContactItem> {
    return this.map { it.toContactItem() }
}