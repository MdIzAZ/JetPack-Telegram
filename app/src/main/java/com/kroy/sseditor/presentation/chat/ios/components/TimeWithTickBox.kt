package com.kroy.sseditor.presentation.chat.ios.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroy.ssediotor.R
import com.kroy.sseditor.presentation.theme.COLOR_PINK
import com.kroy.sseditor.presentation.theme.CustomRobotoMediumFontFamily
import com.kroy.sseditor.presentation.theme.TelegramLight
import com.kroy.sseditor.utils.Utils.convertLettersToUppercase
import com.kroy.sseditor.utils.Utils.removeLeadingZero

@Preview(showBackground = true)
@Composable
fun TimeWithTickBox(
    modifier: Modifier = Modifier,
    isTextMessage: Boolean= true,
    time: String ="03:45 PM",
    isSender: Boolean = false
) {
    Row(
        modifier = modifier
            .then(
                if (!isTextMessage) Modifier
                    .padding(4.dp)
                    .background(
                        Color(0x80000000),
                        RoundedCornerShape(25.dp)
                    )
                    .padding(vertical = 2.dp, horizontal = 4.dp)
                else Modifier
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = removeLeadingZero(time),
            fontFamily = CustomRobotoMediumFontFamily,
            fontWeight = FontWeight.Thin,
            fontSize = (10.5f).sp,
            color = if (isTextMessage) Color(0xFF979797) else Color.Unspecified
        )

        if (isSender) {
            Icon(
                painter = painterResource(id = R.drawable.ic_single_tick), // Replace with your single tick icon resource
                contentDescription = "Single Tick",
                tint = Color.White,
                modifier = Modifier
                    .padding(start = 2.dp, top = 2.dp)
                    .size(12.dp) // Adjust size as needed
            )
        }
    }
}