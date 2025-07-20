package com.kroy.sseditor.presentation.sevenday.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kroy.ssediotor.R
import com.kroy.sseditor.presentation.theme.Telegram

@Composable
fun IntervalSelection(
    interval: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Interval", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)

        Spacer(modifier = Modifier.size(4.dp))

        Row(
            modifier = Modifier
                .height(40.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(MaterialTheme.colorScheme.primary),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            IconButton(onClick = onDecrease) {
                Icon(
                    painter = painterResource(R.drawable.ic_decrease),
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = "Decrease"
                )
            }
            Text(
                text = interval.toString(),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(onClick = onIncrease) {
                Icon(
                    imageVector = Icons.Default.Add,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = "Increase"
                )
            }
        }
    }


}