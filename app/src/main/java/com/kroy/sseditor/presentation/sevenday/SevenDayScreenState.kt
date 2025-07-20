package com.kroy.sseditor.presentation.sevenday

import com.kroy.sseditor.domain.models.ClientTimes
import com.kroy.sseditor.domain.models.ContactItem
import com.kroy.sseditor.domain.models.IntervalGroup

data class SevenDayScreenState(
    val name: String = "",
    val id: Int = 0,
    val isLoading: Boolean = false,
    val times: List<ClientTimes> = listOf(
        ClientTimes("08:00 AM", "08:30 AM", 5),
        ClientTimes("09:00 AM", "09:30 AM", 5),
        ClientTimes("10:00 AM", "10:30 AM", 5),
        ClientTimes("11:00 AM", "11:30 AM", 5),
        ClientTimes("12:00 PM", "12:30 PM", 5),
        ClientTimes("01:00 PM", "01:30 PM", 5),
        ClientTimes("02:00 PM", "02:30 PM", 5)
    ),
    val intervalGroups: List<IntervalGroup> = listOf(IntervalGroup(0, Int.MAX_VALUE, 0)),
    val contactItems: List<ContactItem> = emptyList()
)
