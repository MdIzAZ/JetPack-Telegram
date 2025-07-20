package com.kroy.sseditor.presentation.common_components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.kroy.ssediotor.R

@Preview
@Composable
fun DynamicIsland(modifier: Modifier = Modifier, onLongPress: () -> Unit = {}) {

    val context = LocalContext.current

    Row(modifier = modifier) {

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .height(35.dp)
                .widthIn()
                .width(160.dp)
                .background(Color.Black, shape = RoundedCornerShape(50.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            vibrate(context)
                            onLongPress()
                        }
                    )
                }
        ) {
            Image(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 4.dp, horizontal = 2.dp)
                    .rotate(0f)
                    .size(18.dp),
                painter = painterResource(id = R.drawable.hotspot_bold),
                contentDescription = "Hotspot logo "
            )

        }

        Spacer(modifier = Modifier.weight(1f))
    }
}


fun vibrate(context: Context) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = ContextCompat.getSystemService(context, VibratorManager::class.java)
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        ContextCompat.getSystemService(context, Vibrator::class.java)
    }

    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            if (vibrator != null) {
                @Suppress("DEPRECATION")
                vibrator.vibrate(200)
            }
        }
    } catch (e: Exception) {
        Log.e("Vibrate", "Vibration failed: ${e.message}")
    }
}