package com.kroy.sseditor.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroy.ssediotor.R
import com.kroy.sseditor.models.ChatItem
import com.kroy.sseditor.ui.theme.CustomBoldTypography
import com.kroy.sseditor.ui.theme.CustomMediumTypography
import com.kroy.sseditor.ui.theme.CustomRegularFontFamily
import com.kroy.sseditor.ui.theme.CustomComfortaaFontFamily
import com.kroy.sseditor.ui.theme.CustomRobotoMediumFontFamily
import com.kroy.sseditor.ui.theme.Dimens
import com.kroy.sseditor.ui.theme.RandomBgColorPairs
import com.kroy.sseditor.ui.theme.Telegram
import com.kroy.sseditor.ui.theme.TelegramDark
import com.kroy.sseditor.ui.theme.UnreadMessages
import com.kroy.sseditor.utils.SelectedClient
import com.kroy.sseditor.utils.Utils
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(
    clientName: String,
    r2clickedCount: Int,
    r1clickedCount: Int,
    chats: List<ChatItem>
) {


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val initialTime = remember {
                Utils.parseTimeString(SelectedClient.time)
            }

            Log.d("time set->", "parse $initialTime")
            val randomInitialTime = remember {
                if (SelectedClient.primeAccounts.contains(SelectedClient.clientName)) {
                    initialTime.plusMinutes(1)
                } else {
                    Utils.generateRandomTime(initialTime, 1, 15) // Utils has +1 for max internally
                }
            }
            // Top Status Bar
            StatusBar(randomInitialTime.plusMinutes(1))

            // Chat List (middle content)
            ChatListUI(
                modifier = Modifier
                    .weight(1f),// Fills the remaining space right after the status bar,
                clientName,
                chats,
                randomInitialTime,
                r2clickedCount = r2clickedCount,
                r1clickedCount = r1clickedCount
            )

            // Bottom Navigation Bar (fixed at the bottom)
            BottomNavBar(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(

                                Color.LightGray.copy(alpha = 0.1f),
                                Color.LightGray.copy(alpha = 0.1f),

                                )
                        )
                    )
                    .fillMaxWidth()
                //.align(Alignment.BottomCenter)

            )
        }

//        // Bottom Navigation Bar (fixed at the bottom)
//        BottomNavBar(
//            modifier = Modifier
//                .background(
//                    brush = Brush.verticalGradient(
//                        colors = listOf(
//
//                            Color.LightGray.copy(alpha = 0.1f),
//                            Color.LightGray.copy(alpha = 0.1f),
//
//                            )
//                    )
//                )
//                .fillMaxWidth()
//                //.align(Alignment.BottomCenter)
//
//        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatusBar(randomInitialTime: LocalTime) {

    Column(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(

                        Color.LightGray.copy(alpha = 0.1f),
                        Color.LightGray.copy(alpha = 0.1f),

                        )
                )
            )
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 8.dp) // Padding only at the sides and top
    ) {
        // First Row - Status (Time, Signal, Battery)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Time

            Text(
                text = Utils.removeLeadingZero(
                    randomInitialTime.format(
                        DateTimeFormatter.ofPattern(
                            "hh:mm"
                        )
                    )
                ),
                color = Color.White,
                fontSize = 15.sp,
                letterSpacing = 1.sp,
                style = CustomMediumTypography.titleMedium,
                fontWeight = FontWeight.W700,
                modifier = Modifier.padding(start = 20.dp)
            )


            Spacer(modifier = Modifier.width(20.dp))

            // Telegram Logo and Title in Rounded Box
            // Box that spans between Time and Network Status
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Black, shape = RoundedCornerShape(14.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp),

                ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
//                    modifier = Modifier
//                        .width(100.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.hotspot_bold),
                        contentDescription = "Hotspot logo ",
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 2.dp)
                            .rotate(0f)
                            .size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(17.dp))

            // Status Icons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(end = 13.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_signal2),
                    contentDescription = "Signal",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = "5G",
                    fontSize = 13.sp,
                    fontFamily = CustomRegularFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 2.dp)
                )
                //   Icon(painterResource(id = R.drawable.ic_wifi), contentDescription = "Wi-Fi", tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(7.dp))
                Icon(
                    painter = painterResource(id = Utils.getBatteryImage(SelectedClient.dayName)),
                    contentDescription = "Battery",
                    tint = Color.White,
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(0f)
                )
            }
        }

        // Second Row - Telegram Title & Unread Tabs
        Row(
            modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth()
                .padding(top = 8.dp) // Only top padding between first and second row
        ) {
            Text(
                text = "Edit",
                fontFamily = CustomRobotoMediumFontFamily,
                color = UnreadMessages, // Assuming you have defined TelegramDark
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Chats",
                    color = Color.White,
                    style = CustomBoldTypography.titleMedium,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 12.dp, bottom = 8.dp)
                        .align(Alignment.CenterVertically)
                )
                Image(
                    painter = painterResource(id = R.drawable.bluetick1),
                    contentDescription = "Telegram Logo",
                    modifier = Modifier
                        .padding(top = 2.dp, start = 4.dp)

                        .size(15.dp)
                )
            }

            Row(
                modifier = Modifier.align(Alignment.CenterVertically),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_dotted),
                    contentDescription = "Search",
                    tint = TelegramDark,
                    modifier = Modifier
                        .rotate(180f)
                        .size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "More options",
                    tint = TelegramDark,
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .size(20.dp)
                )
            }
        }

        // Third Row - Tabs with "Unread" centered vertically
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp,
                    bottom = 4.dp
                ),  // Only top padding between first and second row
            verticalAlignment = Alignment.CenterVertically, // Center content vertically in the row
            horizontalArrangement = Arrangement.Start // Spread the content across the width of the row
        ) {
            val spacerValue = 10.dp
            Spacer(modifier = Modifier.width(spacerValue))
            // All tab
            Text(
                text = "All",
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                letterSpacing = (0.5f).sp,

                fontSize = Dimens.ChatScreenCategoryTextSize,
                fontFamily = CustomRobotoMediumFontFamily,
                modifier = Modifier.padding(end = 10.dp, bottom = 12.dp)
            )
            Spacer(modifier = Modifier.width(spacerValue))


            // Personal tab
            Row(
                modifier = Modifier
                    .padding(end = 5.dp, bottom = 7.dp)
            ) {
                Text(
                    text = "Members",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (0.5f).sp,
                    fontSize = Dimens.ChatScreenCategoryTextSize,
                    fontFamily = CustomRobotoMediumFontFamily,
                    modifier = Modifier.padding(end = 3.dp, bottom = 2.dp)
                )
                Box(modifier = Modifier.padding(top = 0.dp)) {
                    BadgeBoxSmall(Random.nextInt(50, 100))
                }


            }
            Spacer(modifier = Modifier.width(spacerValue))


            // "Unread" tab at the center, vertically and horizontally
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, // Center content horizontally in the column
                verticalArrangement = Arrangement.Center // Center content vertically in the column
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,

                    modifier = Modifier.padding(bottom = 7.dp)
                ) {
                    Text(
                        text = "Unread",
                        color = Telegram,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (0.5f).sp,
                        fontSize = Dimens.ChatScreenCategoryTextSize,
                        fontFamily = CustomRobotoMediumFontFamily,
                        modifier = Modifier.padding(end = 3.dp, bottom = 3.dp)
                    )
                    Box(modifier = Modifier.padding(top = 0.dp)) {
                        BadgeBoxSmall(Random.nextInt(50, 100))
                    }
                }
                Divider(
                    color = Telegram,
                    thickness = 2.dp,
                    modifier = Modifier
                        .width(80.dp)  // Adjust width based on content
                        .padding(top = 0.dp, end = 2.dp, start = 0.dp)
                )
            }
            Spacer(modifier = Modifier.width(spacerValue))


            // Channels tab
            Row(
                modifier = Modifier
                    .padding(end = 1.dp, bottom = 7.dp)
            ) {
                Text(
                    text = "Channel",
                    maxLines = 1,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (0.5f).sp,
                    fontSize = Dimens.ChatScreenCategoryTextSize,
                    fontFamily = CustomRobotoMediumFontFamily,
                    modifier = Modifier.padding(end = 3.dp, bottom = 2.dp)
                )
                Box(modifier = Modifier.padding(top = 0.dp)) {
                    BadgeBoxSmall(Random.nextInt(50, 100))
                }
            }
        }
    }
}


@Composable
fun BottomNavBar(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(top = 4.dp)

                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,

            ) {
            // Contacts column
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_contacts),
                    contentDescription = "Contacts",
                    tint = Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "Contacts",
                    color = Color.Gray,
                    fontSize = Dimens.ChatScreenBottomBarTextSize,
                    fontFamily = CustomRobotoMediumFontFamily,
                    fontWeight = FontWeight.Thin,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Chats column with badge overlay
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_pending_msg_white),
                        contentDescription = "Pending messages",
                        tint = UnreadMessages.copy(0.72f),
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "Chats",
                        color = UnreadMessages,
                        fontSize = Dimens.ChatScreenBottomBarTextSize,
                        fontWeight = FontWeight.Thin,
                        fontFamily = CustomRobotoMediumFontFamily,
                        maxLines = 1,
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)

                        .offset(x = 9.dp, y = (0.5f).dp) // Adjust as needed for exact positioning
                        .wrapContentWidth()
                        .background(Color(0xFFF35959), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
//                    Text(
//                        text = Utils.getTotalUnreadMessages(SelectedClient.dayName),
//                      //  text = "1K",
//                        color = Color.White,
//                        fontSize = 11.sp,
//                        fontFamily = CustomRobotoMediumFontFamily,
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis,
//                        modifier = Modifier.padding(horizontal = 4.dp)
//                    )
                }
            }

            // Settings column with profile picture
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
            ) {
                val imagePainter = SelectedClient.backgroundImage?.takeIf { it.isNotEmpty() }?.let {
                    Utils.base64ToBitmap(it)?.asImageBitmap()?.let { bitmap ->
                        BitmapPainter(bitmap)
                    }
                } ?: painterResource(id = R.drawable.default_pic)

                Image(
                    painter = imagePainter,
                    contentScale = ContentScale.Crop,
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = "Settings",
                    color = Color.Gray,
                    fontSize = Dimens.ChatScreenBottomBarTextSize,
                    fontFamily = CustomRobotoMediumFontFamily,
                    fontWeight = FontWeight.Thin,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }


        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(.4f)
                .padding(top = 20.dp)
                .height(5.dp)
                .background(Color.White)
        )


    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListUI(
    modifier: Modifier = Modifier,
    clientName: String,
    chats: List<ChatItem>,
    randomInitialTime: LocalTime,
    r2clickedCount: Int,
    r1clickedCount: Int,
) {
    // Parse the initial time from the SelectedClient
    val initialTime = remember {
        (randomInitialTime)
    }


    val timeOffsets = remember(chats) {
        val totalChats = chats.size

        val useSmallRange = Random.nextBoolean() // randomly true or false

        val (minRange, maxRange) = if (useSmallRange) {
            1 to minOf(2, totalChats)
        } else {
            5 to minOf(7, totalChats)
        }

        val n1 = Random.nextInt(minRange, maxRange + 1)

        chats.indices.map { index ->
            if (index < n1) 1 else 0
        }
    }


    LazyColumn(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = modifier.fillMaxSize()
    ) {
        itemsIndexed(chats) { index, chat ->
            val adjustedTime = remember(timeOffsets[index]) {
                initialTime.plusMinutes(timeOffsets[index].toLong())
            }

            ChatRow(
                chat = chat,
                clientName = clientName,
                time = adjustedTime,
                r2clickedCount = r2clickedCount,
                r1clickedCount = r1clickedCount,
            )
            Divider(color = Color.Gray, thickness = 0.1.dp)
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatRow(
    clientName: String, chat: ChatItem, time: LocalTime,
    r2clickedCount: Int,
    r1clickedCount: Int
) {
    // Format the time
    val formattedTime = remember {
        Utils.convertLettersToUppercase(
            time.format(DateTimeFormatter.ofPattern("hh:mm a"))
        )
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val rathoreTimes = listOf("01:55 AM", "01:56 AM", "01:57 AM")


            // Profile Image
            val gradientPair = RandomBgColorPairs.random()
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                gradientPair.first,
                                gradientPair.second
                            ) // Apply gradient from the pair
                        )
                    )
            ) {
                if (chat.profileImage != null) {
                    Image(
                        bitmap = chat.profileImage!!.asImageBitmap(),
                        contentDescription = "Profile picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                } else {
                    val initials = chat.name.split(" ").filter { it.isNotBlank() }
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
                        color = Color.White,
                        //   style = MaterialTheme.typography.body1,
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
                        text = chat.name,
                        fontFamily = CustomRobotoMediumFontFamily,
                        fontSize = 14.5.sp,
                        letterSpacing = 0.7.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = if (clientName == "RATHORE 1") rathoreTimes[r1clickedCount - 1]
                               else if (clientName == "RATHORE 2") rathoreTimes[r2clickedCount - 1]
                               else formattedTime,
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
                    if (chat.message.isNullOrEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.default_comment_img),
                                contentDescription = "Photo icon",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Photo",
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
                            text = chat.message,
                            color = Color.Gray,
                            fontFamily = CustomRobotoMediumFontFamily,
                            fontSize = 14.7.sp,
                            fontWeight = FontWeight.Thin,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Display BadgeBox if there are unread messages, with slight downward offset
                    BadgeBox(
                        unreadCount = chat.unreadCount,
                        size = 18,
                        modifier = Modifier.offset(y = 4.dp)
                    )
                }

                // Add a divider after each chat item
                Divider(
                    color = Color.Gray.copy(alpha = 0.3f),
                    thickness = 0.18.dp,
                    modifier = Modifier.padding(top = 18.dp)
                )
            }
        }
    }
}


@Composable
fun BadgeBox(unreadCount: Int, size: Int, modifier: Modifier = Modifier) {
    Box(

        modifier = modifier
            .wrapContentSize()
            .padding(top = 10.dp)


    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(size.dp)
                .background(Color.Gray, CircleShape)
        ) {
            Text(
                text = unreadCount.toString(),
                fontFamily = CustomRobotoMediumFontFamily,
                fontSize = (12.7f).sp,
                fontWeight = FontWeight.Thin,
                modifier = Modifier
                    .padding(bottom = 3.dp),
                color = Color.Black
            )
        }
    }

}


@Composable
fun BadgeBoxSmall(unreadCount: Int) {
    // Pending Messages Box
    Box(
        modifier = Modifier
            .padding(top = 0.dp, bottom = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
        ) {
            Text(
                text = "$unreadCount",
                fontFamily = CustomRobotoMediumFontFamily,
                fontWeight = FontWeight.Thin,
                fontSize = (11f).sp,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(UnreadMessages, RoundedCornerShape(18.dp))
                    .padding(horizontal = 7.5.dp, vertical = 0.dp)
            )

        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(
    showBackground = true,
    device = "spec:width=1440px,height=3200px,dpi=800" // This simulates a 6.7-inch screen with 1440x3200 resolution and 560 dpi
)
@Composable
fun TelegramScreenPreview() {
    val context = LocalContext.current
    val chats = listOf(
        ChatItem(
            "Akash Gupta",
            "Hi",
            SelectedClient.time,
            Utils.getBitmapFromResource(context, R.drawable.b),
            Random.nextInt(2, 5)
        ),
        ChatItem(
            "Effi",
            "Hey",
            SelectedClient.time,
            Utils.getBitmapFromResource(context, R.drawable.b),
            Random.nextInt(2, 5)
        ),
        ChatItem("Anil Wuryavanshi", "", SelectedClient.time, null, Random.nextInt(2, 5)),
        ChatItem(
            "EXCEPTION",
            "Hey",
            SelectedClient.time,
            Utils.getBitmapFromResource(context, R.drawable.f),
            Random.nextInt(2, 10)
        ),
        ChatItem(
            "Jsvindr Sng",
            "https://t.me/+i_voE00fHsMOODA9 jhkjhj ijiuoiu kokpokpok kokpo jpokkkjk;jopk jkjkj kkljkj",
            SelectedClient.time,
            null,
            Random.nextInt(2, 5)
        ),
        ChatItem(
            "Zpple",
            "https://t.me/+qnGC9Zd2csJkZDU9",
            SelectedClient.time,
            null,
            Random.nextInt(2, 5)
        ),
        ChatItem(
            "Binary Trading Trader",
            "H this is the best text you can find from eleaborate he fghfgfhfhfghgfhgfhgffghgfhgffghgfhgfhfghgfhgfhfghgfhgfhgfhgfhgfh ",
            SelectedClient.time,
            Utils.getBitmapFromResource(context, R.drawable.a),
            Random.nextInt(2, 10)
        ),
        ChatItem(
            "Binary Trading Trader",
            "Ftgmn...",
            SelectedClient.time,
            Utils.getBitmapFromResource(context, R.drawable.a),
            Random.nextInt(2, 5)
        ),
        ChatItem(
            "Binary Trading Trader",
            "Ftgmn...",
            SelectedClient.time,
            Utils.getBitmapFromResource(context, R.drawable.a),
            Random.nextInt(2, 5)
        ),
        ChatItem(
            "Binary Trading Trader",
            "Ftgmn...",
            SelectedClient.time,
            Utils.getBitmapFromResource(context, R.drawable.a),
            Random.nextInt(2, 5)
        ),
        ChatItem(
            "Binary Trading Trader",
            "Ftgmn...",
            SelectedClient.time,
            Utils.getBitmapFromResource(context, R.drawable.a),
            Random.nextInt(2, 5)
        ),
        ChatItem(
            "Binary Trading Trader",
            "Ftgmn...",
            SelectedClient.time,
            Utils.getBitmapFromResource(context, R.drawable.a),
            Random.nextInt(2, 5)
        ),
        ChatItem(
            "Binary Trading Trader",
            "Ftgmn...",
            SelectedClient.time,
            Utils.getBitmapFromResource(context, R.drawable.a),
            Random.nextInt(2, 5)
        ),
        ChatItem(
            "Binary Trading Trader",
            "Ftgmn...",
            SelectedClient.time,
            Utils.getBitmapFromResource(context, R.drawable.a),
            Random.nextInt(2, 5)
        ),
        ChatItem(
            "Tronix Bot",
            "🦴🦴🦴🦴🦴",
            SelectedClient.time,
            Utils.getBitmapFromResource(context, R.drawable.b),
            Random.nextInt(2, 5)
        )
    )
    ChatScreen("RATHORE 1", 3, 2, chats)
}
