package com.kroy.sseditor.presentation.contact_list.ios.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroy.sseditor.presentation.theme.CustomRobotoMediumFontFamily
import com.kroy.sseditor.presentation.theme.IosFolderColor
import com.kroy.sseditor.presentation.theme.UnreadMessages

@Preview
@Composable
fun BadgeBoxSmall(
    modifier: Modifier = Modifier,
    unreadCount: Int = 4,
    color: Color = IosFolderColor
) {

    Box(
        modifier = modifier
            .wrapContentSize()
    ) {
        if (unreadCount == 0) return

        Text(
            text = "$unreadCount",
            //text = "237",
            fontFamily = CustomRobotoMediumFontFamily,
            fontWeight = FontWeight.Thin,
            fontSize = (10f).sp,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .background(
                    color,
                    if (unreadCount >= 10) RoundedCornerShape(24.dp) else RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 6.dp, vertical =  0.dp)
        )

    }

}