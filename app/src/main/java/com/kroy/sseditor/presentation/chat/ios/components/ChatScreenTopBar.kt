package com.kroy.sseditor.presentation.chat.ios.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroy.ssediotor.R
import com.kroy.sseditor.presentation.common_components.IOSNotificationBar
import com.kroy.sseditor.presentation.theme.CustomComfortaaFontFamily
import com.kroy.sseditor.presentation.theme.CustomGray
import com.kroy.sseditor.presentation.theme.CustomPurple
import com.kroy.sseditor.presentation.theme.CustomRobotoMediumFontFamily
import com.kroy.sseditor.presentation.theme.RandomBgColorPairs
import com.kroy.sseditor.presentation.theme.UnreadMessages
import com.kroy.sseditor.utils.Utils
import kotlinx.coroutines.delay


@Preview(showBackground = true)
@Composable
fun ChatScreenTopBar(
    time: String = "12:00 PM",
    contactName: String = "Ronaldo",
    numberOfUnseenMessages: Int = 12,
    batteryIcon:Int = R.drawable.battery70,
    batteryPercentage:Int = 70,
    contactPic: Bitmap? = null,
    color: Color? = null,
    onBackClick: () -> Unit = {}
) {

    var lastSeenStatus by remember { mutableStateOf("Online") }

    LaunchedEffect(Unit) {
        val delay = (0..5).random()
        delay(delay.times(1000).toLong())
        lastSeenStatus = "Last seen today"
    }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 88.dp)
            .background(color = CustomGray.copy(alpha = 0.85f))
            .graphicsLayer {
                shape = RoundedCornerShape(0.dp)
                clip = true
            }
    ) {


        IOSNotificationBar(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            time = time,
            batteryPercentage = batteryPercentage,
            batteryIcon = batteryIcon,
            onLongPress = {}
        )


        //2nd line of status bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 15.dp)
                .wrapContentHeight(),

            ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {

                Row(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                        .clickable { onBackClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Back Button
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        modifier = Modifier
                            .size(24.dp),
                        contentDescription = "Back",
                        tint = UnreadMessages
                    )


                    // Pending Messages Box
                    Box(
                        modifier = Modifier
                            .padding(top = 1.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .wrapContentSize()
                        ) {
                            Text(
                                text = "$numberOfUnseenMessages",
                                fontFamily = CustomRobotoMediumFontFamily,
                                fontWeight = FontWeight.Thin,
                                fontSize = (12f).sp,
                                color = Color.White,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .background(UnreadMessages, RoundedCornerShape(10.dp))
                                    .padding(horizontal = 8.dp, vertical = 1.dp)
                            )

                        }
                    }

                }
                // Spacer to create space between the Row and Column
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                        .align(Alignment.Center)
                        .width(5.dp)
                ) // Adjust the height as needed


                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center // Use Center to avoid space between items
                ) {
                    Text(
                        text = contactName,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = CustomRobotoMediumFontFamily,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = -(0.2f).sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 10.dp, top = 8.dp)
                    )
                    Text(
                        text = lastSeenStatus,
                        color = Color(0xFFAAACAF),
                        fontFamily = CustomRobotoMediumFontFamily,
                        fontWeight = FontWeight.Thin,
                        fontSize = 12.sp,
                        letterSpacing = 0.3.sp,
                        modifier = Modifier.padding(
                            top = 2.dp,
                            start = 10.dp,
                            bottom = 5.dp
                        ), // No top padding here
                        textAlign = TextAlign.Center
                    )
                }



                Box(
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.CenterEnd)
                        .size(37.dp)
                        .clip(CircleShape)
                        .background(color = color ?: Color.Cyan) // Default background color
                ) {
                    if (contactPic != null) {
                        Image(
                            bitmap = contactPic.asImageBitmap(),
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
        }

    }
}