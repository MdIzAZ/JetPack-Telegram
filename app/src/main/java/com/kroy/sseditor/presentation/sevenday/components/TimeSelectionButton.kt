package com.kroy.sseditor.presentation.sevenday.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kroy.sseditor.presentation.theme.Telegram

@Composable
fun TimeSelectionButton(
    modifier: Modifier = Modifier,
    title: String,
    time: String = "12:00",
    onTimerBtnClick: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)

        Spacer(modifier = Modifier.size(4.dp))

        Box(
            modifier = modifier
                .padding(horizontal = 4.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(color = MaterialTheme.colorScheme.primary)
                .clickable { onTimerBtnClick() }
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = time,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.size(5.dp))
                Icon(
                    modifier = Modifier,
                    imageVector = Icons.Default.Edit,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = "Favorite"
                )
            }


        }
    }


}