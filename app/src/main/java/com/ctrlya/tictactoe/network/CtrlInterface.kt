package com.ctrlya.tictactoe.network

import io.ktor.websocket.*
import kotlinx.serialization.json.*
import kotlinx.serialization.json.Json.Default.encodeToJsonElement


interface CtrlProtocol {
    suspend fun chat(message : String)
    suspend fun event(event : GameStatus)
}

suspend fun messageReceive(message: Frame.Text, ctrlProtocol: CtrlProtocol){
    val json = Json.parseToJsonElement(message.readText())
    val messageTypeString = json.jsonObject["type"].toString().trim('"')
    val dataJson = json.jsonObject["data"]!!

    when (messageTypeString){
        Int::class.simpleName ->{
            ctrlProtocol.chat(Json.decodeFromJsonElement<String>(dataJson))
        }
    }
}


inline fun<reified T> sendCtrlProtocol(data: T) = buildJsonObject {
    put("type", data!!::class.simpleName)
    put("data", Json.encodeToJsonElement<T>(data))
}.toString()

enum class Choose {
    Rock,
    Paper,
    Scissors
}

enum class GameStatus {
    Win,
    Lose,
    Draw
}

fun status(yourChoose: Choose, enemyChoose: Choose): GameStatus {
    return when {
        yourChoose == Choose.Rock && enemyChoose == Choose.Scissors -> GameStatus.Lose
        yourChoose == Choose.Scissors && enemyChoose == Choose.Rock -> GameStatus.Lose
        yourChoose == Choose.Paper && enemyChoose == Choose.Scissors -> GameStatus.Lose
        yourChoose == enemyChoose -> GameStatus.Draw
        else -> GameStatus.Win
    }
}