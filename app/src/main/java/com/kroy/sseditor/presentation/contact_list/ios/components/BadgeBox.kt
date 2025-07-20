package com.kroy.sseditor.presentation.contact_list.ios.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroy.sseditor.presentation.theme.CustomRobotoMediumFontFamily
import com.kroy.sseditor.presentation.theme.UnreadNoBox


//@Preview(showSystemUi = true)
@Composable
fun BadgeBox(
    modifier: Modifier = Modifier,
    boxColor: Color ,
    unreadCount: Int = 12
) {

    Text(
        modifier = modifier
            .then(if (unreadCount == 0) Modifier.alpha(0f) else Modifier)
            .height(28.dp)
            .widthIn(min = if (unreadCount < 10) 28.dp else 36.dp)
            .padding(4.dp)
            .background(
                color = boxColor,
                shape = if (unreadCount < 10) CircleShape else RoundedCornerShape(16.dp)
            )
            .padding(4.dp),
        text = unreadCount.toString(),
        fontFamily = CustomRobotoMediumFontFamily,
        fontSize = (10).sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Thin,
        color = Color.White
    )

}