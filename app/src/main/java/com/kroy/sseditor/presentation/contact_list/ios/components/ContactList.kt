package com.kroy.sseditor.presentation.contact_list.ios.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kroy.sseditor.domain.models.ContactItem


//@Preview(showSystemUi = true)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContactList(
    modifier: Modifier = Modifier,
    chats: List<ContactItem> ,
    onContactClick: (Int) -> Unit = {}
) {



    LazyColumn(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = modifier.fillMaxSize()
    ) {
        itemsIndexed(chats) { index, chat ->
            ContactRow(
                contact = chat,
                time = chat.uiTime,
                onContactClick = { onContactClick(chat.id) }
            )
        }
    }

}