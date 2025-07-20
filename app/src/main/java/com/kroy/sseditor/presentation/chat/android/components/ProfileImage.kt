package com.kroy.sseditor.presentation.chat.android.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroy.sseditor.presentation.theme.CustomComfortaaFontFamily
import com.kroy.sseditor.utils.SelectedContact.contactName

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    color: Color?,
    bitmap: Bitmap?
) {
    Box(
        modifier = modifier
            .wrapContentWidth()
            .size(40.dp)
            .clip(CircleShape)
            .background(color = color ?: Color.Cyan) // Default background color
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
        } else {

            val initials = contactName.split(" ").filter { it.isNotBlank() }
                .let { words ->
                    when {
                        words.size == 1 -> words.first().firstOrNull()?.uppercase()
                            ?: "N"

                        words.size > 1 -> {
                            val firstInitial =
                                words.first().firstOrNull()?.uppercase() ?: ""
                            val lastInitial =
                                words.last().firstOrNull()?.uppercase() ?: ""
                            "$firstInitial$lastInitial"
                        }

                        else -> "N"
                    }
                }

            Text(
                text = initials,
                fontSize = 20.sp,

                fontFamily = CustomComfortaaFontFamily,
                fontWeight = FontWeight.W800,
                color = Color.White,
                //   style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .padding(bottom = 2.dp)
                    .align(Alignment.Center)

            )
        }
    }
}