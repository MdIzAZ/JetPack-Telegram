package com.kroy.sseditor.presentation.client.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kroy.sseditor.domain.models.Client

@Composable
fun ClientList(
    clients: List<Client>,
    onClick: (Client) -> Unit,
    onEditClick: (Client) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(0.2.dp)
    ) {

        items(clients) { client ->
            ClientItem(client = client, onClick, onEditClick)
        }
    }
}