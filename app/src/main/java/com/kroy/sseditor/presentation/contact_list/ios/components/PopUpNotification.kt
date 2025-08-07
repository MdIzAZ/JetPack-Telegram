package com.kroy.sseditor.presentation.contact_list.ios.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroy.ssediotor.R
import com.kroy.sseditor.domain.models.ContactItem
import com.kroy.sseditor.domain.models.NonTextMessage
import com.kroy.sseditor.domain.models.dummyContacts
import com.kroy.sseditor.presentation.theme.CustomComfortaaFontFamily
import com.kroy.sseditor.presentation.theme.CustomRobotoMediumFontFamily
import com.kroy.sseditor.presentation.theme.UnreadNoBox
import com.kroy.sseditor.utils.Utils.removeLeadingZero


@Preview(showBackground = true)
@Composable
fun PopUpNotification(
    modifier: Modifier = Modifier,
    contact: ContactItem = dummyContacts[0]
) {

    val context = LocalContext.current



    Row(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 28.dp)
            .height(80.dp)
            .fillMaxWidth()
            .background(shape = RoundedCornerShape(16.dp), color = Color.Black)
            .padding(start = 12.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(color = contact.color ?: Color.Cyan)
        ) {

            if (contact.profileImage != null) {
                Image(
                    bitmap = contact.profileImage.asImageBitmap(),
                    contentDescription = "Profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(58.dp)
                )
            } else {
                val initials = contact.name.split(" ").filter { it.isNotBlank() }
                    .let { words ->
                        when {
                            words.size == 1 -> words.first().firstOrNull()?.uppercase() ?: "N"
                            words.size > 1 -> {
                                val firstInitial =
                                    words.first().firstOrNull()?.uppercase() ?: ""
                                val lastInitial = words.last().firstOrNull()?.uppercase() ?: ""
                                "$firstInitial$lastInitial"
                            }

                            else -> "N"
                        }
                    }


                Text(
                    text = initials,
                    fontSize = 29.sp,
                    fontFamily = CustomComfortaaFontFamily,
                    fontWeight = FontWeight.W900,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)

                )
            }
        }


        Spacer(modifier = Modifier.width(8.dp))

        // Column for the text and badge content
        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = contact.name,
                fontFamily = CustomRobotoMediumFontFamily,
                fontSize = (15.5).sp,
                letterSpacing = 0.7.sp,
                fontWeight = FontWeight.W700,
                color = Color.White,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 2.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                if (contact.messages.last().text.isBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {

                        val randomEmoji = listOf("😂", "🤬", "👍", "💖").random()

                        Text(
                            text = when (contact.messages.last().nonTextMessage) {
                                is NonTextMessage.Image -> "Photo"
                                is NonTextMessage.Sticker -> "Sticker"
                                is NonTextMessage.Gif -> randomEmoji + "Sticker"
                                null -> ""
                            },
                            color = Color.Gray,
                            fontFamily = CustomRobotoMediumFontFamily,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Thin,
                            maxLines = 1,
                            modifier = Modifier.padding(top = 5.dp),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                } else {
                    Text(
                        text = contact.messages.last().text,
                        color = Color.Gray,
                        fontFamily = CustomRobotoMediumFontFamily,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Thin,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 4.dp)
                    )
                }


            }

        }

        val bitmap = contact.messages.last().nonTextMessage?.let {
            when (it) {
                is NonTextMessage.Image -> it.bitmap
                is NonTextMessage.Sticker -> it.bitmap
                is NonTextMessage.Gif -> null
            }
        }


        bitmap?.asImageBitmap()?.let {

            Image(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxSize(),
                bitmap = it,
//              painter = painterResource(R.drawable.b),
                contentDescription = "Photo icon",
                contentScale = ContentScale.Crop,
            )

        }
    }
}

