package com.kroy.sseditor.presentation.contact_list.ios.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroy.ssediotor.R
import com.kroy.sseditor.presentation.theme.CustomRobotoMediumFontFamily
import com.kroy.sseditor.presentation.theme.Dimens
import com.kroy.sseditor.presentation.theme.IosFolderColor
import com.kroy.sseditor.presentation.theme.UnreadMessages
import com.kroy.sseditor.utils.SelectedClient
import com.kroy.sseditor.utils.Utils


@Preview(showBackground = true)
@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    count: Int = 4,
    onLongPress: () -> Unit = {}
) {
    Column(
        modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 83.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(top = 4.dp)
                .defaultMinSize(minHeight = 49.dp)
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

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onLongPress() }
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call",
                    tint = Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "Calls",
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
                        tint = IosFolderColor,
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
//                    val options = listOf("")
                    Text(
//                        text = count.toString(),
                        text = "1.1K",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontFamily = CustomRobotoMediumFontFamily,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }

            // Settings column with profile picture
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
            ) {
                val imagePainter = SelectedClient.clientImage?.takeIf { it.isNotEmpty() }?.let {
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

        Spacer(Modifier.height(24.dp))

//        Box(
//            modifier = Modifier
//                .align(Alignment.CenterHorizontally)
//                .fillMaxWidth(.4f)
//                .padding(top = 20.dp)
//                .height(5.dp)
//                .background(Color.White)
//        )


    }

}