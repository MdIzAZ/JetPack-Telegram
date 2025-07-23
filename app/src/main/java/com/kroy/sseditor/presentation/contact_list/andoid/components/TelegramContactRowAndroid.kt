package com.kroy.sseditor.presentation.contact_list.andoid.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
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
import com.kroy.sseditor.presentation.contact_list.ios.components.BadgeBox
import com.kroy.sseditor.presentation.theme.CustomComfortaaFontFamily
import com.kroy.sseditor.presentation.theme.CustomRobotoMediumFontFamily
import com.kroy.sseditor.presentation.theme.DarkBluishGray
import com.kroy.sseditor.presentation.theme.TelegramBlack

@Preview
@Composable
fun TelegramContactRowAndroid(
    modifier: Modifier = Modifier,
    contact: ContactItem = ContactItem(
        id = 0,
        name = "Haaland",
        messages = emptyList(),
        unreadCount = 4
    ),
    time: String = "12:00 PM",
    onContactClick: () -> Unit = {}
) {
    val context = LocalContext.current


    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(TelegramBlack)
            .padding(start = 8.dp, end = 8.dp)
            .clickable { onContactClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(color = contact.color ?: Color.Cyan)
        ) {


            if (contact.profileImage != null) {
                Image(
                    bitmap = contact.profileImage.asImageBitmap(),
                    contentDescription = "Profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(60.dp)
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
                    fontSize = 28.sp,
                    fontFamily = CustomComfortaaFontFamily,
                    fontWeight = FontWeight.W900,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier
                        .padding(bottom = 5.dp)
                        .align(Alignment.Center)

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
                    fontSize = 14.5.sp,
                    letterSpacing = 0.7.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 10.dp)
                )
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = time,
                    fontSize = 14.sp,
                    fontFamily = CustomRobotoMediumFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
            }

            // Row for the message and badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (contact.messages.last().text.isBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        val bitmap = contact.messages.last().nonTextMessage?.let {
                            when (it) {
                                is NonTextMessage.Image -> it.bitmap
                                is NonTextMessage.Sticker -> it.bitmap
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
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when(contact.messages.last().nonTextMessage) {
                                is NonTextMessage.Image -> "Photo"
                                is NonTextMessage.Sticker -> "Sticker"
                                null -> ""
                            },
                            color = Color.Gray,
                            fontFamily = CustomRobotoMediumFontFamily,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Thin,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                } else {
                    Text(
                        text = contact.messages.last().text,
                        color = Color.Gray,
                        fontFamily = CustomRobotoMediumFontFamily,
                        fontSize = 14.7.sp,
                        fontWeight = FontWeight.Thin,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Display BadgeBox if there are unread messages, with slight downward offset
                BadgeBox(
                    modifier = Modifier.padding(top = 4.dp),
                    unreadCount = contact.unreadCount ?: 0,
                    boxColor = Color.Gray
                )
            }

            // Add a divider after each chat item
            Divider(
                modifier = Modifier.padding(top = 10.dp),
                thickness = 0.5.dp,
                color = Color.Black
            )
        }
    }
}


