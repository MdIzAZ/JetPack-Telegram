package com.kroy.sseditor.domain.models

import android.net.Uri

sealed class MessageType {
    data class Text(val txt: String) : MessageType()
    data class Image(val img: Uri) : MessageType()
    data class Sticker(val sticker: Uri) : MessageType()
}