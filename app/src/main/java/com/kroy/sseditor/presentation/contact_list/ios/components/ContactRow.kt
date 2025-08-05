package com.kroy.sseditor.presentation.contact_list.ios.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import com.kroy.sseditor.domain.models.dummyChatMessages
import com.kroy.sseditor.presentation.theme.CustomComfortaaFontFamily
import com.kroy.sseditor.presentation.theme.CustomRobotoMediumFontFamily
import com.kroy.sseditor.presentation.theme.UnreadNoBox
import com.kroy.sseditor.utils.Utils.removeLeadingZero

@Preview(showBackground = true)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContactRow(
    contact: ContactItem = ContactItem(
        id = 1,
        name = "John Doe",
        profileImage = null,
        messages = dummyChatMessages,
        unreadCount = 0,
        color = Color.Cyan,
        timeRemainingInSec = 4
    ),
    time: String = "09:00 AM",
    onContactClick: () -> Unit = {}
) {

    val context = LocalContext.current


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp)
            .clickable { onContactClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box() {

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

            if (contact.timeRemainingInSec>0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(color = Color.Black, shape = CircleShape)
                        .padding(2.dp)
                        .background(color = Color(0xFF6DCB5D), shape = CircleShape)
                )
            }
        }


        Spacer(modifier = Modifier.width(8.dp))

        // Column for the text and badge content
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Row for the name and time
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = contact.name,
                    fontFamily = CustomRobotoMediumFontFamily,
                    fontSize = (15.5).sp,
                    letterSpacing = 0.7.sp,
                    fontWeight = FontWeight.W700,
                    color = Color.White,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 7.dp)
                )
                Text(
                    text = removeLeadingZero(time),
                    fontSize = 13.sp,
                    fontFamily = CustomRobotoMediumFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
            }

            // Row for the last message and badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                if (contact.messages.last().text.isBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        val bitmap = contact.messages.last().nonTextMessage?.let {
                            when (it) {
                                is NonTextMessage.Image -> it.bitmap
                                is NonTextMessage.Sticker -> null
                                is NonTextMessage.Gif -> null
                            }
                        }
                        bitmap?.asImageBitmap()?.let {
                            Image(
                                bitmap = it,
                                contentDescription = "Photo icon",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        if (bitmap != null) {
                            Spacer(modifier = Modifier.width(8.dp))
                        }

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
//                            .padding(top = 5.dp)
                    )
                }

                BadgeBox(
                    modifier = Modifier.padding(top = 5.dp),
                    unreadCount = contact.unreadCount ?: 0,
                    boxColor = UnreadNoBox
                )
            }

            // Add a divider after each chat item
            HorizontalDivider(
                modifier = Modifier.padding(top = 10.dp),
                thickness = 1.dp,
                color = Color.Gray.copy(alpha = 0.3f)
            )
        }
    }

}