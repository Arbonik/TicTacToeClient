package com.ctrlya.tictactoe.network.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomResponse(val roomId: String)
