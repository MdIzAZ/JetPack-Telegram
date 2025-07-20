package com.kroy.sseditor.utils

import com.kroy.sseditor.domain.models.ClientTimes

object SelectedClient {
    var clientId: Int = 0
    var clientImage: String = ""
    var backgroundImage: String = ""
    var clientName: String = ""
    var dayName: String = "Day 2"
    var clientTimes: ClientTimes = ClientTimes("08:00 AM", "08:00 AM", 5)
    val primeAccounts = arrayOf("RATHORE 1","RATHORE 2")

}