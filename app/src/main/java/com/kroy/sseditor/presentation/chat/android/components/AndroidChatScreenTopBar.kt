package com.kroy.sseditor.presentation.chat.android.components

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroy.sseditor.presentation.theme.BluishGray
import com.kroy.sseditor.utils.Utils
import kotlinx.coroutines.delay

@Preview(showBackground = true)
@Composable
fun AndroidChatScreenTopBar(
    modifier: Modifier = Modifier,
    name: String = "Roberto Baggio",
    profilePic: Bitmap? = null,
    color: Color? = null,
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var lastSeenStatus by remember { mutableStateOf("Online") }

    LaunchedEffect(Unit) {
        delay(10_000)
        lastSeenStatus = "Last seen today"
    }

    Row(
        modifier = modifier.padding(top = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = { onBackClick() }) {
            Icon(
                modifier = Modifier,
                tint = Color.White,
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        ProfileImage(
            modifier = Modifier.padding(start = 4.dp, end = 16.dp),
            color = color,
            bitmap = profilePic
        )


        Column(
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp).weight(1f),
            verticalArrangement = Arrangement.spacedBy(1.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = name,
                fontSize = 15.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 17.sp
            )

            Text(
                text = lastSeenStatus,
                fontSize = 12.sp,
                color = Color.Gray,
                lineHeight = 11.sp
            )


        }


        Icon(
            tint = Color.White,
            imageVector = Icons.Default.Call,
            contentDescription = "Call"
        )

        Spacer(Modifier.width(22.dp))

        Icon(
            tint = Color.White,
            imageVector = Icons.Default.MoreVert,
            contentDescription = "More"
        )

        Spacer(Modifier.width(12.dp))

    }


}