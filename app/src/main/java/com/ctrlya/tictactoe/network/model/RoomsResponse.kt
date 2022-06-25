package com.ctrlya.tictactoe.network.model

import kotlinx.serialization.Serializable

@Serializable
data class RoomsResponse(
    val items: List<Item>? = listOf()
)