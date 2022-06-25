package com.ctrlya.tictactoe.core.domain

import kotlinx.serialization.Serializable

@Serializable
data class BattlefieldSettings(
    val maxWidth : Int,
    val maxHeight : Int,
    val winSequenceLength : Int,
    val resizable : Boolean = true, // false <- поле сразу будет в полный размер
)