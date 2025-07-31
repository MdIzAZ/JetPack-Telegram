package com.kroy.sseditor.presentation.chat.ios.components

import android.util.Base64
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kroy.sseditor.data.mapper.ddd
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream


@Preview
@Composable
fun GifMessage(
    modifier: Modifier = Modifier,
    time: String = "09:32 PM",
    gif: String = ddd.substringAfter("base64,"),
    lastReceiverMsgTime: String = "12:09 PM",
    isSender: Boolean = false
) {

    Row(
        modifier = modifier
            .padding(horizontal = 14.dp)
            .fillMaxSize(),
        horizontalArrangement = if (isSender) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .padding(2.dp)
                .wrapContentSize()
        ) {

            val lottieJson = remember(gif) { decompressTgsBase64(gif) }
            val compositionState = rememberLottieComposition(
                spec = lottieJson?.let { LottieCompositionSpec.JsonString(it) }!!
            )
            val composition = compositionState.value

            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                isPlaying = true
            )



            if (composition != null) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = modifier.size(100.dp)
                )
            } else {
                Box(modifier = modifier.size(100.dp)) {
                    Text("Gif Sticker")
                }
            }



            TimeWithTickBox(
                modifier = Modifier.align(Alignment.BottomEnd),
                isTextMessage = false,
                isSender = isSender,
                time = if (isSender) time else lastReceiverMsgTime
            )
        }
    }
}



fun decompressTgsBase64(base64: String): String? {
    return try {
        val compressedBytes = Base64.decode(base64, Base64.DEFAULT)
        val inputStream = GZIPInputStream(ByteArrayInputStream(compressedBytes))
        val outputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var len: Int

        while (inputStream.read(buffer).also { len = it } > 0) {
            outputStream.write(buffer, 0, len)
        }

        outputStream.toString(Charsets.UTF_8.name())
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

