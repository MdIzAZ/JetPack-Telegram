package com.kroy.sseditor.domain.models

import android.graphics.Bitmap

sealed class NonTextMessage {
    data class Sticker(val bitmap: Bitmap) : NonTextMessage()
    data class Image(val bitmap: Bitmap) : NonTextMessage()
}