package com.kroy.sseditor.screens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kroy.ssediotor.R
import com.kroy.sseditor.models.ApiResponse
import com.kroy.sseditor.models.ChatItem
import com.kroy.sseditor.models.ChatMessage
import com.kroy.sseditor.models.ContactItem
import com.kroy.sseditor.ui.theme.CustomTypography
import com.kroy.sseditor.ui.theme.Primary
import com.kroy.sseditor.utils.ContactScrollPositionManager
import com.kroy.sseditor.utils.SelectedClient
import com.kroy.sseditor.utils.SelectedClient.clientName
import com.kroy.sseditor.utils.Utils
import com.kroy.sseditor.utils.Utils.CaptureAndSaveComposable
import com.kroy.sseditor.utils.Utils.base64ToBitmap
import com.kroy.sseditor.utils.Utils.getYesterdaysDateFormatted
import com.kroy.sseditor.viewmodels.ContactViewModel
import com.kroy.sseditor.viewmodels.SharedViewModel
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random


@RequiresApi(Build.VERSION_CODES.O)
val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

@RequiresApi(Build.VERSION_CODES.O)
fun addRandomMinutesToTime(initialTime: String, min: Int, max: Int): String {
    val trimmedTime = initialTime.trim()

    return try {
        val parsedTime = LocalTime.parse(trimmedTime, timeFormatter)

        // Generate a random number of minutes within the range
        val randomMinutes = Random.nextInt(min, max)

        // Adjust the parsed time by the random minutes
        val adjustedTime = parsedTime.plusMinutes(randomMinutes.toLong())

        // Format and return the adjusted time in 12-hour format
        adjustedTime.format(timeFormatter)
    } catch (e: DateTimeParseException) {
        // In case of error, return the original time
        initialTime
    }
}


@SuppressLint("UnrememberedMutableState")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContactScreen(
    onAddContact: () -> Unit = {},
    onEditClick: (ContactItem) -> Unit = {},
    onTransferContact: () -> Unit,
    sharedViewModel: SharedViewModel
) {


    val context = LocalContext.current
    val contactViewModel: ContactViewModel = hiltViewModel()
    val isLoading: State<Boolean> = contactViewModel.isLoading.collectAsState()
    val allContacts: State<ApiResponse.AllContactResponse?> =
        contactViewModel.filteredContactResponse.collectAsState()
    val contactDetails: State<ApiResponse.ContactDetailsResponse?> =
        contactViewModel.filteredgetContactDetailsResponse.collectAsState()
    val randomContacts: State<ApiResponse.RandomContactsResponse?> =
        contactViewModel.filteredRandomContactsResponse.collectAsState()
    // Remember and save the scroll index and offset manually
    // Retrieve scroll index and offset from ViewModel
    // Retrieve scroll index and offset from the singleton object
    val scrollIndex = ContactScrollPositionManager.scrollIndex
    val scrollOffset = ContactScrollPositionManager.scrollOffset


    var r1clickedCount by rememberSaveable { mutableStateOf(0) }
    var r2clickedCount by rememberSaveable { mutableStateOf(0) }
    var rathoreTimes by remember { mutableStateOf<List<String>>(emptyList()) }

    // Use rememberLazyListState to initialize with global scroll position
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = scrollIndex,
        initialFirstVisibleItemScrollOffset = scrollOffset
    )

    // Track and save the current scroll index and offset globally
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                ContactScrollPositionManager.scrollIndex = index
                ContactScrollPositionManager.scrollOffset = offset
            }
    }

    LaunchedEffect(SelectedClient.clientName) {
        val times = getIncreasedTimes(SelectedClient.time)
        rathoreTimes = times
    }

    Log.d(
        "contact loading ->",
        " loading value ${isLoading.value} ,${contactDetails.value?.data?.contactId} "
    )

    if (contactDetails.value?.data != null && isLoading.value) {
        Log.d("contact loading ->", " Entered image saving  ")

        val comments = mutableStateListOf<String>().apply {
            add(contactDetails.value?.data!!.comment1)
            add(contactDetails.value?.data!!.comment2)
            add(contactDetails.value?.data!!.comment3)
        }

        // Prepare other parameters for CaptureAndSaveComposable
        val contactName = contactDetails.value?.data!!.contactName
        val contactPic = base64ToBitmap(contactDetails.value?.data!!.contactImage)

        // Start with the initial time
        var currentTime = addRandomMinutesToTime(SelectedClient.time, 1, 2)

        // Generate times for each message with specific differences
        val messages = comments
            .filter { it.trim().isNotEmpty() }
            .mapIndexed { index, trimmedComment ->
                // Update the time for each message based on the index
                currentTime = when (index) {
                    0 -> addRandomMinutesToTime(
                        currentTime,
                        1,
                        2
                    ) // 1st message: 1-2 min difference
                    1 -> addRandomMinutesToTime(
                        currentTime,
                        2,
                        3
                    ) // 2nd message: 2-3 min difference
                    2 -> addRandomMinutesToTime(
                        currentTime,
                        5,
                        7
                    ) // 3rd message: 5-7 min difference
                    else -> currentTime
                }

                ChatMessage(trimmedComment.trim(), currentTime, isSender = true)
            }

        // Update SelectedClient.time for future use
        //  SelectedClient.time = addRandomMinutesToTime(SelectedClient.time, 2, 30)

        val initialTimeString = currentTime // New time after random increment
        val backgroundBitmap = base64ToBitmap(SelectedClient.backgroundImage)
        val senderImage = base64ToBitmap(contactDetails.value?.data!!.uploadedImage)
        val userReplySticker = base64ToBitmap(SelectedClient.clientImage)

        Log.d("time set->", "contact $initialTimeString")

        // Call the saving function
        CaptureAndSaveComposable(
            contactName = contactName,
            contactPic = contactPic,
            messages = messages,
            initialTimeString = currentTime,
            backgroundBitmap = backgroundBitmap,
            senderImage = senderImage,
            userReplySticker = userReplySticker,
            contactViewModel
        )
    }

    // Remember the list of ChatItem, initializing it based on the randomContacts value
    val chatItems =
        remember { mutableStateListOf<ChatItem>() } // Initialize as a mutable state list

    // Update the chatItems when randomContacts changes and isLoading is true
    Log.d("random contacts ->", "${randomContacts.value?.data}")
    if (randomContacts.value?.data != null && isLoading.value) {
        chatItems.clear() // Clear previous items before adding new ones
        for (contact in randomContacts?.value?.data!!) {
            // Create a ChatItem from RandomContact
            val chatItem = ChatItem(
                name = contact.contactName,
                message = contact.comment1, // or any other comment you want to use as message
                time = getYesterdaysDateFormatted(), // Get yesterday's date
                profileImage = Utils.base64ToBitmap(contact.contactImage), // Convert the image string to Bitmap if needed
                unreadCount = Random.nextInt(
                    1,
                    4
                ) // Set unread count, you can modify this based on your logic
            )
            // Add the ChatItem to the list
            chatItems.add(chatItem)
        }
        Utils.generateNewChatScreen(
            chatList = chatItems,
            contactViewModel = contactViewModel,
            rathoreTime = if (SelectedClient.clientName == "RATHORE 1") rathoreTimes[(r1clickedCount-1)%3]
            else if (SelectedClient.clientName == "RATHORE 2") rathoreTimes[(r2clickedCount-1)%3]
            else null,
        )

    }

    if (allContacts.value?.data != null) { // setting all contacts
        sharedViewModel.setContacts(allContacts.value?.data!!)
    }


    var searchQuery by remember { mutableStateOf("") }

    // Filtered contacts based on search query
    val filteredContacts = allContacts.value?.data?.filter {
        it.contactName.contains(searchQuery, ignoreCase = true)
    } ?: emptyList()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Forward button


                // Heading
                Text(
                    text = "Contacts",
                    style = CustomTypography.titleLarge.copy(
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .weight(1f)
                )
                IconButton(
                    onClick = {
                        onTransferContact()
                    },
                    modifier = Modifier

                        .padding(start = 10.dp, end = 5.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_forward),
                        contentDescription = "Forward",
                        modifier = Modifier
                            .padding(5.dp)
                            .size(30.dp),

                        colorFilter = ColorFilter.tint(Primary)
                    )
                }

                // Generate button
                IconButton(
                    onClick = {

                        if (clientName == "RATHORE 1") {
                            r1clickedCount++
                        } else if (clientName == "RATHORE 2") {
                            r2clickedCount++
                        }

                        contactViewModel.setLoading(true)
                        contactViewModel.getRandomContacts(
                            clientName = clientName,
                            SelectedClient.clientId,
                            SelectedClient.dayName,
                            context
                        )
                    },
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(50.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_download),
                        contentDescription = "Generate",
                        modifier = Modifier.size(60.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }

            // Search box
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                maxLines = 1,
                label = { Text("Search Contacts") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                )
            )

            ContactList(
                contacts = filteredContacts,
                onEditClick = onEditClick,
                contactViewModel = contactViewModel,
                listState = listState
            )
        }

        if (isLoading.value) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.Gray
            )
        }

        FloatingActionButton(
            onClick = onAddContact,
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color.White,
            shape = CircleShape
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "Add Contact",
                tint = Primary
            )
        }
    }

}

@Composable
fun ContactList(
    contacts: List<ContactItem>,
    onEditClick: (ContactItem) -> Unit,
    contactViewModel: ContactViewModel,
    listState: LazyListState // Receive the LazyListState here
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(0.2.dp),
        state = listState // Set the LazyListState to retain scroll position
    ) {
        items(contacts) { contact ->
            ContactItem(contact = contact, onEditClick, contactViewModel)
        }
    }
}

@Composable
fun ContactItem(
    contact: ContactItem,
    onEditClick: (ContactItem) -> Unit,
    contactViewModel: ContactViewModel
) {
    val context = LocalContext.current
    // val isLoading: State<Boolean> = contactViewModel.isLoading.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${contact.contactId}. ${contact.contactName}",
                modifier = Modifier
                    .padding(start = 4.dp),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = {
                contactViewModel.setLoading(true)
                // fetch  the data and generate the screenshot
                //TODO()  - Use @CustomeTelegramLayput to generate the screenshot
                contactViewModel.getcontactDetails(
                    contactId = contact.contactId,
                    context
                )


            },
            modifier = Modifier
                .size(40.dp)
                .background(Primary, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.width(15.dp))

        IconButton(
            onClick = {
                // returning the same contact on edit
                onEditClick(contact)
            },
            modifier = Modifier
                .size(40.dp)
                .background(Primary, CircleShape)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "Edit Image",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ContactScreenPreview() {
    val v = hiltViewModel<SharedViewModel>()
    ContactScreen(onAddContact = {}, onEditClick = {}, onTransferContact = {}, v)
}


fun getIncreasedTimes(timeStr: String): List<String> {
    val format = SimpleDateFormat("hh:mm a", Locale.US)
    val baseDate = format.parse(timeStr)!!
    val result = mutableListOf<String>()

    for (i in 0..2) {
        val calendar = Calendar.getInstance()
        calendar.time = baseDate
        calendar.add(Calendar.MINUTE, i)
        result.add(format.format(calendar.time))
    }

    return result
}
