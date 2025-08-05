package com.kroy.sseditor.presentation.sevenday

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kroy.sseditor.data.mapper.toContactItemList
import com.kroy.sseditor.domain.models.ChatMessage
import com.kroy.sseditor.domain.models.IntervalGroup
import com.kroy.sseditor.domain.models.MessageType
import com.kroy.sseditor.domain.models.NonTextMessage
import com.kroy.sseditor.domain.models.dummyContacts
import com.kroy.sseditor.domain.repo.ContactRepo
import com.kroy.sseditor.presentation.chat.ChatScreenState
import com.kroy.sseditor.presentation.contact_list.ContactListScreenState
import com.kroy.sseditor.utils.SelectedClient
import com.kroy.sseditor.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import kotlin.math.ceil
import kotlin.math.max

@HiltViewModel
class SelectTimeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val contactRepo: ContactRepo,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        startOnlineCountdown()
    }

    private var clockJob: Job? = null
    private var chatUpdateJob: Job? = null
    private var notificationUpdateJob: Job? = null

    private val _sevenDayScreenState = MutableStateFlow(SevenDayScreenState())
    val sevenDayScreenState = _sevenDayScreenState.asStateFlow()

    private val _contactListScreenState = MutableStateFlow(ContactListScreenState())
    val contactListScreenState = _contactListScreenState.asStateFlow()

    private val _chatScreenState = MutableStateFlow(ChatScreenState())
    val chatScreenState = _chatScreenState.asStateFlow()

    /* Client Screen */

    fun setFolders(folders: List<Pair<String, Int>>) {
        _contactListScreenState.update {
            it.copy(folders = folders)
        }
    }


    /*  Seven day   */

    fun setIntervals(groups: List<IntervalGroup>) {
        viewModelScope.launch {
            try {
                _sevenDayScreenState.update {
                    it.copy(intervalGroups = groups)
                }
            } catch (e: Exception) {
                Log.d("izaz", e.message ?: "Unknown error")
            }
        }

    }

    fun updateTriggerTime(time: String, dayIndex: Int) {
        viewModelScope.launch {
            val timeList = sevenDayScreenState.value.times.toMutableList()
            val oldTimeItem = timeList[dayIndex]
            val newTimeItem = oldTimeItem.copy(triggerTime = time)
            timeList[dayIndex] = newTimeItem

            _sevenDayScreenState.update { it ->
                it.copy(times = timeList)
            }
        }
    }

    fun updateUiTime(time: String, dayIndex: Int) {
        viewModelScope.launch {
            val timeList = sevenDayScreenState.value.times.toMutableList()
            val oldTimeItem = timeList[dayIndex]
            val newTimeItem = oldTimeItem.copy(uiTime = time)
            timeList[dayIndex] = newTimeItem

            _sevenDayScreenState.update { it ->
                it.copy(times = timeList)
            }
        }
    }

    fun updateIntervalTime(intervalChangeType: Int, dayIndex: Int) {
        viewModelScope.launch {
            val timeList = sevenDayScreenState.value.times.toMutableList()
            val oldTimeItem = timeList[dayIndex]
            val oldInterval = oldTimeItem.interval
            val newInterval = when (intervalChangeType) {
                0 -> if (oldInterval > 5) oldInterval - 1 else oldInterval
                1 -> oldInterval + 1
                else -> oldInterval
            }

            val newTimeItem = oldTimeItem.copy(interval = newInterval)
            timeList[dayIndex] = newTimeItem

            _sevenDayScreenState.update { it ->
                it.copy(times = timeList)
            }
        }

    }

    fun setClientDetails(
        name: String,
        id: Int
    ) {
        viewModelScope.launch {
            _sevenDayScreenState.update { it ->
                it.copy(name = name, id = id)
            }
        }
    }

    fun loadContactItems(onDataFetched: () -> Unit) {

        viewModelScope.launch {

            try {
                _sevenDayScreenState.update { it.copy(isLoading = true) }

                _contactListScreenState.update {
                    it.copy(battery = Utils.getRandomBatteryPair())
                }

//                val contacts =
//                    contactRepo.getRandomContacts().data?.toContactItemList() ?: emptyList()

                val contacts = dummyContacts

                _sevenDayScreenState.update {
                    it.copy(contactItems = sevenDayScreenState.value.contactItems + contacts)
                }

                _sevenDayScreenState.update { it.copy(isLoading = false) }
                onDataFetched()

            } catch (e: Exception) {
                Log.d("izaz", e.message ?: "Unknown error")
            }

        }


    }

    fun onLongPress() {
        viewModelScope.launch {
            try {

//                val newContacts =
//                    contactRepo.getRandomContacts().data?.toContactItemList() ?: emptyList()

                val newContacts = dummyContacts

                val allContacts = sevenDayScreenState.value.contactItems + newContacts

                _sevenDayScreenState.update {
                    it.copy(contactItems = allContacts)
                }

                val currentQueue = contactListScreenState.value

            } catch (e: Exception) {
                Log.d("izaz", e.message ?: "Unknown error")
            }

        }
    }


    /*  Contact List   */
    @RequiresApi(Build.VERSION_CODES.O)
    fun startShowingChatItemsWithDelay(
        uiTime: String,
        triggerTime: LocalTime,
        isNotificationEnabled: Boolean
    ) {

        if (contactListScreenState.value.isListUpdatingStarted) return

        _contactListScreenState.update {   // Should show notification
            it.copy(isNotificationEnabled = isNotificationEnabled)
        }

        if (contactListScreenState.value.isNotificationEnabled) {
            startShowingNotificationWithDelay(
                delayBetweenTwo = 1000,
                triggerTime = triggerTime
            )
        }

        startClock(initialTime = uiTime)

        val today = LocalDate.now()
        val dateTime = LocalDateTime.of(today, triggerTime)
        val triggerMillis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        chatUpdateJob?.cancel()
        chatUpdateJob = viewModelScope.launch {

            val delayMillis = triggerMillis - System.currentTimeMillis()
            Log.d("izaz", "Delay: $delayMillis")

            if (delayMillis > 0) delay(delayMillis)

            var lastShownCount = 0

            sevenDayScreenState
                .map { it.contactItems }
                .distinctUntilChanged()
                .collect { currentList ->

                    val newItems = currentList.drop(lastShownCount).reversed()

                    for ((idx, item) in newItems.withIndex()) {

                        val contactItem = item.copy(
                            uiTime = _contactListScreenState.value.notificationBarTime,
                            unreadCount = item.unreadCount,
                            timeRemainingInSec = (0..15).random()
                        )

//                        val totalUnreadMessages =
//                            contactListScreenState.value.totalUnreadMessages + (item.unreadCount
//                                ?: 0)

                        val totalUnreadMessages =
                            contactListScreenState.value.totalUnreadMessages + 1

                        _contactListScreenState.update { state ->
                            state.copy(
                                contactItems = listOf(contactItem) + state.contactItems,
                                totalUnreadMessages = totalUnreadMessages,
//                                currentNotification = if (isNotificationEnabled) contactItem else null
                            )
                        }

                        _chatScreenState.update {
                            it.copy(numberOfUnseenMessages = totalUnreadMessages)
                        }


                        val itemIndex = lastShownCount + idx
                        val interval = sevenDayScreenState.value.intervalGroups.find { group ->
                            itemIndex in group.start!!..group.end!!
                        }?.interval ?: 1000L // Default fallback delay

                        delay(interval)
                        lastShownCount++
                    }

                }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun startShowingNotificationWithDelay(
        delayBetweenTwo: Long = 1000,
        triggerTime: LocalTime
    ) {
        val today = LocalDate.now()
        val dateTime = LocalDateTime.of(today, triggerTime)
        val triggerMillis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        notificationUpdateJob?.cancel()
        notificationUpdateJob = viewModelScope.launch {

            val delayMillis = triggerMillis - System.currentTimeMillis()
            if (delayMillis > 0) delay(delayMillis)

            var lastShownCount = 0

            sevenDayScreenState
                .map { it.contactItems }
                .distinctUntilChanged()
                .collect { currentList ->

                    val newItems = currentList.drop(lastShownCount).reversed()

                    var idx = 0
                    while (idx < newItems.size) {
                        val item = newItems[idx]
                        Log.d("izaz", "${item.name}")


                        _contactListScreenState.update { state ->
                            Log.d("izaz", "${state.notificationItems.size}")

                            state.copy(
                                notificationItems = listOf(item) + state.notificationItems
                            )
                        }

                        val itemIndex = lastShownCount + idx
                        val interval = sevenDayScreenState.value.intervalGroups.find { group ->
                            itemIndex in group.start!!..group.end!!
                        }?.interval ?: 1000L


                        val jump =
                            if (interval.toInt() == 0) 1 else ceil((delayBetweenTwo / interval).toDouble()).toInt()
                                .coerceAtLeast(1)

                        Log.d("izaz", "Jump: $jump")
                        Log.d("izaz", "Interval: $interval")


                        val haveToDelay = max(delayBetweenTwo, interval)
                        delay(haveToDelay)

                        idx += jump
                        lastShownCount += jump
                    }

                    delay(3000)
                    _contactListScreenState.update {
                        it.copy(notificationItems = listOf(null) + it.notificationItems)
                    }
                }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun startClock(initialTime: String) {
        clockJob?.cancel()
        clockJob = viewModelScope.launch {

            _contactListScreenState.update {
                it.copy(notificationBarTime = initialTime, isListUpdatingStarted = true)
            }

            val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)


            var time = try {
                LocalTime.parse(initialTime, formatter)
            } catch (e: Exception) {
                Log.e("izaz", "Problem in formatting: $e")
                LocalTime.now()
            }

            while (true) {

                delay(60 * 1000L) // Delay for 1 minute
                time = time.plusMinutes(1)
                val formattedTime = time.format(formatter)

                _contactListScreenState.update { state ->
                    state.copy(notificationBarTime = formattedTime)
                }

                _chatScreenState.update {
                    it.copy(notificationBarTime = formattedTime)
                }


            }
        }
    }

    fun clearContactScreenState() {
        viewModelScope.launch {
            clockJob?.cancel()
            chatUpdateJob?.cancel()
            clockJob = null
            chatUpdateJob = null
            _contactListScreenState.value = ContactListScreenState()
            _sevenDayScreenState.update {
                it.copy(contactItems = emptyList())
            }
        }
    }

    fun saveJsonToPublicDownloads() {
        val json = """
        {
          "All": 67,
          "Members": 27,
          "Unread": 23,
          "Channel": 8
        }
    """.trimIndent()

        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val jsonFile = File(downloadsDir, "sse-folders.json")

        if (!jsonFile.exists()) {
            try {
                jsonFile.writeText(json)
                Toast.makeText(context, "Saved to Public Downloads", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Failed to save JSON", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "File already exists", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startOnlineCountdown() {
        viewModelScope.launch {
            while (true) {
                delay(1000L) // wait 1 second
                _contactListScreenState.update { currentState ->
                    val updatedItems = currentState.contactItems.map { item ->
                        if (item.timeRemainingInSec > 0) {
                            item.copy(timeRemainingInSec = item.timeRemainingInSec - 1)
                        } else item
                    }
                    currentState.copy(contactItems = updatedItems)
                }
            }
        }
    }


    /*    Chat Screen   */
    fun fetchChatScreenDetails(contactId: Int) {
        viewModelScope.launch {

            val contactItem = contactListScreenState.value.contactItems.find { it.id == contactId }

            val chatMessages = contactItem?.messages ?: emptyList()

            //modify unread msg count of an Contact
            val updatedList = contactListScreenState.value.contactItems.map {
                if (it.id == contactId) it.copy(unreadCount = 0)
                else it
            }
            _contactListScreenState.update {
                it.copy(contactItems = updatedList)
            }


            //reduce total unread messages count
            _contactListScreenState.update {

                val reduceBy = if (contactItem?.unreadCount == 0) 0 else 1

                it.copy(
//                    totalUnreadMessages = contactListScreenState.value.totalUnreadMessages -
//                            (contactItem?.unreadCount ?: 0)

                    totalUnreadMessages = contactListScreenState.value.totalUnreadMessages - reduceBy
                )
            }

            //fetch chat messages for the contact
            _chatScreenState.update {
                it.copy(
                    contactId = contactId,
                    contactName = contactItem?.name ?: "Unknown",
                    contactPic = contactItem?.profileImage,
                    messages = chatMessages,
                    backgroundImage = Utils.base64ToBitmap(SelectedClient.backgroundImage),
                    notificationBarTime = contactListScreenState.value.notificationBarTime,
                    lastMessageTime = contactItem?.uiTime ?: "04:25 AM",
                    timeRemaining = contactItem?.timeRemainingInSec ?: 0,
                    battery = contactListScreenState.value.battery,
                    numberOfUnseenMessages = contactListScreenState.value.totalUnreadMessages,
                    backgroundColor = contactItem?.color
                )
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addNewChatItem(contactId: Int, msgType: MessageType) {
        when (msgType) {
            is MessageType.Image -> addImageOrSticker(
                contactId,
                msgType.img,
                MessageType.Image(msgType.img)
            )

            is MessageType.Sticker -> addImageOrSticker(
                contactId,
                msgType.sticker,
                MessageType.Sticker(msgType.sticker)
            )

            is MessageType.Text -> addTextMessage(recipientId = contactId, text = msgType.txt)
        }
    }

    private fun addImageOrSticker(contactId: Int, uri: Uri, type: MessageType) {

        viewModelScope.launch {
            try {

                val bitmap = uriToBitmap(uri)

                val nonTextMessage = when (type) {
                    is MessageType.Image -> NonTextMessage.Image(bitmap ?: return@launch)
                    is MessageType.Sticker -> NonTextMessage.Sticker(bitmap ?: return@launch)
                    is MessageType.Text -> null
                }


                val id = chatScreenState.value.messages.lastIndex + 1

                nonTextMessage.let {

                    val chatItem = ChatMessage(
                        id = id,
                        recipientId = contactId,
                        isSender = true,
                        isTextMessage = false,
                        text = "",
                        timestamp = chatScreenState.value.notificationBarTime,
                        nonTextMessage = nonTextMessage
                    )


                    // add new chat item to the specific Contacts Message List
                    val updatedContactList = contactListScreenState.value.contactItems.map {
                        if (it.id == contactId) {
                            it.copy(messages = it.messages + chatItem)
                        } else {
                            it
                        }
                    }


                    _contactListScreenState.update {
                        it.copy(contactItems = updatedContactList)
                    }


                    _chatScreenState.update {
                        it.copy(messages = it.messages + listOf(chatItem))
                    }

                }
            } catch (e: Exception) {
                Log.d("izaz", e.message ?: "Unknown error")
            }
        }

    }

    private fun addTextMessage(text: String, recipientId: Int) {

        if (text.isBlank()) return

        val id = chatScreenState.value.messages.lastIndex + 1

        val newChatItem = ChatMessage(
            id = id,
            recipientId = recipientId,
            isTextMessage = true,
            isSender = true,
            text = text,
            timestamp = chatScreenState.value.notificationBarTime
        )

        val updatedContactList = contactListScreenState.value.contactItems.map {
            if (it.id == recipientId) {
                it.copy(messages = it.messages + newChatItem)
            } else {
                it
            }
        }

        _contactListScreenState.update {
            it.copy(contactItems = updatedContactList)
        }

        _chatScreenState.update {
            it.copy(messages = it.messages + listOf(newChatItem))
        }
    }

    private fun uriToBitmap(uri: Uri, maxWidth: Int = 2048, maxHeight: Int = 2048): Bitmap? {
        return try {
            // 1. Decode bounds only (to get dimensions)
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }

            context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, options)
            }

            // 2. Calculate scaling
            val (originalWidth, originalHeight) = options.outWidth to options.outHeight
            var inSampleSize = 1
            if (originalHeight > maxHeight || originalWidth > maxWidth) {
                val halfHeight = originalHeight / 2
                val halfWidth = originalWidth / 2
                while ((halfHeight / inSampleSize) >= maxHeight && (halfWidth / inSampleSize) >= maxWidth) {
                    inSampleSize *= 2
                }
            }

            // 3. Decode actual image with scaling
            val scaledOptions = BitmapFactory.Options().apply {
                this.inSampleSize = inSampleSize
            }

            context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, scaledOptions)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun editTextMessage(recipientId: Int, messageId: Int, msg: String) {

        val updatedContactList = contactListScreenState.value.contactItems.map { contactItem ->
            if (contactItem.id == recipientId) {
                val updatedMessages = contactItem.messages.map { chatMsg ->
                    if (chatMsg.id == messageId && !chatMsg.isSender) {
                        chatMsg.copy(text = msg)
                    } else chatMsg
                }

                _chatScreenState.update {
                    it.copy(messages = updatedMessages)
                }

                contactItem.copy(messages = updatedMessages)


            } else {
                contactItem
            }
        }

        _contactListScreenState.value = contactListScreenState.value.copy(
            contactItems = updatedContactList
        )

    }


}