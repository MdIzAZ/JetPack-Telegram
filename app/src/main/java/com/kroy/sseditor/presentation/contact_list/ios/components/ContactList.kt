package com.kroy.sseditor.presentation.contact_list.ios.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kroy.sseditor.domain.models.ContactItem
import com.kroy.sseditor.domain.models.dummyContacts
import com.kroy.sseditor.utils.Utils.removeLeadingZeroNotMeridian


@Preview()
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContactList(
    modifier: Modifier = Modifier,
    chats: List<ContactItem> = dummyContacts,
    onTopPositionChange: (Boolean) -> Unit,
    onContactClick: (Int) -> Unit = {}
) {

    val listState = rememberLazyListState()
    val isAtTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 &&
                    listState.firstVisibleItemScrollOffset == 0
        }
    }

    LaunchedEffect(isAtTop) {
        onTopPositionChange(isAtTop)
    }

    LazyColumn(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        state = listState,
        modifier = modifier.fillMaxSize()
    ) {
        itemsIndexed(chats, key = { _, chat -> chat.id }) { index, chat ->
            ContactRow(
                contact = chat,
                time = chat.uiTime,
                onContactClick = { onContactClick(chat.id) }
            )
        }
    }

}