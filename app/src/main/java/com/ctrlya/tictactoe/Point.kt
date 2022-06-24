package com.ctrlya.tictactoe

import kotlinx.serialization.Serializable

@Serializable
data class Point(val x : Int, val y : Int)

//data class GamePoint(val position: Point, mark : Mark)