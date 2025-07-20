package com.kroy.sseditor.data.mapper

import com.kroy.sseditor.data.remote.models.ClientResponse
import com.kroy.sseditor.domain.models.Client

fun ClientResponse.toClient(): Client {
    return Client(
        clientId = clientId,
        clientImage = clientImage,
        backgroundImage = backgroundImage,
        clientName = clientName
    )
}


fun List<ClientResponse>.toClientList(): List<Client> {
    return this.map { it.toClient() }
}