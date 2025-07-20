package com.kroy.sseditor.presentation.contact_transfer

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kroy.sseditor.data.remote.models.ApiResponse
import com.kroy.sseditor.data.remote.models.ContactResponse
import com.kroy.sseditor.domain.models.Client
import com.kroy.sseditor.data.remote.models.CopyContactReqBody
import com.kroy.sseditor.presentation.contact_list.remove.ContactViewModel
import com.kroy.sseditor.presentation.theme.Primary


@Preview(showBackground = true)
@Composable
fun PreviewContactManager() {
    val dummyClients = List(8){
        Client(it+1,"Client ${it+1}")
    }

    val dummyContacts = List(100) {
        ContactResponse(it+1,"Contact ${it + 1}")
        }

    ContactTransferScreen(
        allContacts = dummyContacts,
        allClients = dummyClients,
        onTransferComplete = {
        }
    )
}

@Composable
fun ContactTransferScreen(
    allContacts: List<ContactResponse>,
    allClients: List<Client>,
    onTransferComplete: () -> Unit
) {
    val context = LocalContext.current
    val contactViewModel: ContactViewModel = hiltViewModel()
    val isLoading: State<Boolean> = contactViewModel.isLoading.collectAsState()
    val copyContact: State<ApiResponse.CopyContactsResponse?> = contactViewModel.filteredCopyContactsResponse.collectAsState()
    var hasNavigated by remember { mutableStateOf(false) }
    var selectedClient by remember { mutableStateOf(Client(clientId = 0, clientName = "")) }
    var selectedDay by remember { mutableStateOf("") }
    var currentPage by remember { mutableStateOf(1) }
    val pageSize = 50
    val selectedContacts = remember { mutableStateListOf<ContactResponse>() }
    val selectAllState = remember { mutableStateMapOf<Int, Boolean>() }
    val paginatedContacts = allContacts.chunked(pageSize)
    val currentContacts = paginatedContacts.getOrNull(currentPage - 1) ?: emptyList()
    val days = List(7) { "Day ${it + 1}" }

    fun resetPreviousPageItems(previousPage: Int) {
        val previousContacts = paginatedContacts.getOrNull(previousPage - 1) ?: emptyList()
        selectedContacts.removeAll(previousContacts)
        selectAllState[previousPage] = false
    }

    if (copyContact.value?.data != null && !hasNavigated && copyContact.value?.data?.totalTimeTaken!!.isNotEmpty()) {
        Log.d("Transfer Contact ->", "Completed in ${copyContact.value?.data?.totalTimeTaken!!}")
        hasNavigated = true
        contactViewModel.setLoading(false)
        onTransferComplete()
        Toast.makeText(context, "Transfer Completed", Toast.LENGTH_SHORT).show()
        contactViewModel.resetContactState()
    }
    if (copyContact.value?.data != null && !hasNavigated && copyContact.value?.data?.duplicateContact != null ) {
       // Log.d("Transfer Contact ->", "Completed in ${copyContact.value?.data?.totalTimeTaken!!}")
       // hasNavigated = true
        contactViewModel.setLoading(false)
        Toast.makeText(context, "${copyContact.value?.data?.duplicateContact!!.contactName} already exists ", Toast.LENGTH_SHORT).show()
        contactViewModel.resetContactState()
    }

    if (copyContact.value?.data == null  && !hasNavigated && copyContact?.value?.message!=null) {
      //  hasNavigated = true
        contactViewModel.setLoading(false)
       // onTransferComplete()
        Toast.makeText(context, copyContact?.value?.message, Toast.LENGTH_SHORT).show()
        contactViewModel.resetContactState()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
                .then(if (isLoading.value) Modifier.clickable(enabled = false) {} else Modifier)
        ) {
            ClientDropDownMenu(
                allClients = allClients.map { it },
                selectedClient = selectedClient,
                placeholder = "Select Client",
                onClientSelected = { selectedClient = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            DropDownMenu(
                allItems = days,
                selectedItem = selectedDay,
                placeholder = "Select Day",
                onItemSelected = { selectedDay = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.wrapContentSize()
            ) {
                val selectAll = selectAllState[currentPage] ?: false
                Checkbox(
                    checked = selectAll,
                    onCheckedChange = { isChecked ->
                        selectAllState[currentPage] = isChecked
                        if (isChecked) {
                            selectedContacts.addAll(currentContacts)
                        } else {
                            selectedContacts.removeAll(currentContacts)
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Primary,
                        uncheckedColor = Primary,
                        checkmarkColor = Color.White
                    )
                )
                Text(
                    "Select All",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .border(2.dp, Primary)
                    .weight(1f)
            ) {
                items(currentContacts) { contact ->
                    val isChecked = selectedContacts.contains(contact)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(2.dp)
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    selectedContacts.add(contact)
                                } else {
                                    selectedContacts.remove(contact)
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Primary,
                                uncheckedColor = Primary,
                                checkmarkColor = Color.White
                            )
                        )
                        Text(
                            "${contact.contactId}. ${contact.contactName}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 8.dp),
                            color = Primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(2.dp, Primary)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        if (currentPage > 1) {
                            resetPreviousPageItems(currentPage)
                            currentPage--
                        }
                    },
                    enabled = currentPage > 1,
                    colors = ButtonDefaults.buttonColors(Primary)
                ) {
                    Text("Previous", color = Color.White)
                }

                Text(
                    "Page $currentPage / ${paginatedContacts.size}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = Primary
                )

                Button(
                    onClick = {
                        if (currentPage < paginatedContacts.size) {
                            resetPreviousPageItems(currentPage)
                            currentPage++
                        }
                    },
                    enabled = currentPage < paginatedContacts.size,
                    colors = ButtonDefaults.buttonColors(Primary)
                ) {
                    Text("Next", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                enabled = !isLoading.value,
                onClick = {
                    if (selectedContacts.isEmpty()) {
                        Toast.makeText(context, "Please select at least one contact", Toast.LENGTH_SHORT).show()
                    } else if (selectedClient.clientName == "") {
                        Toast.makeText(context, "Please select a client", Toast.LENGTH_SHORT).show()
                    } else if (selectedDay == "") {
                        Toast.makeText(context, "Please select a day", Toast.LENGTH_SHORT).show()
                    } else {
                        contactViewModel.setLoading(true)
                        contactViewModel.copyContacts(
                            CopyContactReqBody(
                                clientId = selectedClient.clientId,
                                contactList = selectedContacts.toList(),
                                dayName = selectedDay
                            ),
                            context
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(19.dp)),
                colors = ButtonDefaults.buttonColors(Primary)
            ) {
                Text(
                    "Transfer",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W700,
                    color = Color.White
                )
            }
        }

        if (isLoading.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary)
            }
        }
    }
}

@Composable
fun ClientDropDownMenu(
    allClients: List<Client>,
    selectedClient: Client?,
    placeholder: String,
    onClientSelected: (Client) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Text(
            text = if (selectedClient?.clientName!!.isNotEmpty()) selectedClient!!.clientName else placeholder,
            fontSize = 18.sp,
            fontWeight = FontWeight.W500,
            modifier = Modifier
                .border(2.dp, Primary)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(18.dp),
            color = Primary
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            allClients.forEach { client ->
                DropdownMenuItem(
                    onClick = {
                        onClientSelected(client) // Pass the entire clientItem object
                        expanded = false
                    },
                    text = {
                        Text(
                            text ="${client.clientId}. ${client.clientName}" ,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W500,
                            color = Color.White
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Primary)
                )
            }
        }
    }
}


@Composable
fun DropDownMenu(
    allItems: List<String>,
    selectedItem: String,
    placeholder: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Text(
            text = if (selectedItem.isNotEmpty()) selectedItem else placeholder,
            fontSize = 18.sp,
            fontWeight = FontWeight.W500,
            modifier = Modifier
                .border(2.dp, Primary)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(18.dp),
            color = Primary
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            allItems.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    },
                    text = {
                        Text(
                            text = item,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W500,
                            color = Color.White
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Primary)
                )
            }
        }
    }
}


