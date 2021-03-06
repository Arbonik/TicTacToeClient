package com.ctrlya.tictactoe.core.data

import kotlinx.serialization.Serializable

@Serializable
data class Point(
    val x: Int = 0,
    val y: Int = 0
)