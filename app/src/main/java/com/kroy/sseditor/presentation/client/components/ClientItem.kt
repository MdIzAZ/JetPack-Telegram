package com.kroy.sseditor.presentation.client.components

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroy.sseditor.domain.models.Client
import com.kroy.sseditor.utils.Utils.base64ToBitmap
import kotlin.math.log

@Composable
fun ClientItem(
    client: Client,
    onClick: (Client) -> Unit,
    onEditClick: (Client) -> Unit
) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(client.clientImage) {
        Log.d("xyz", "client: ${client.clientImage}")
        bitmap = base64ToBitmap(client.clientImage.substringAfter("base64,"))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick(client) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = client.clientName,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .border(
                        border = BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        shape = CircleShape
                    ),
                contentScale = ContentScale.Crop
            )
        } ?: run {
            // Placeholder for loading state or error state
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = client.clientName,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Adding Edit Button
        IconButton(
            onClick = {
                onEditClick(client)
            },
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Client",
                tint = MaterialTheme.colorScheme.onTertiary
            )
        }

        Spacer(modifier = Modifier.width(10.dp))
    }
}