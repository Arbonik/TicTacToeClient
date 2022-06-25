package com.ctrlya.tictactoe.network

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.ctrlya.tictactoe.canvas.TicTacToeView
import com.ctrlya.tictactoe.core.data.Point
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*

interface CtrlProtocol {
    suspend fun point(point: Point)
    suspend fun chat(message: String)
    suspend fun event(event: GameStatus)
}

suspend fun messageReceive(message: Frame.Text, ctrlProtocol: CtrlProtocol) {

    val json = Json.parseToJsonElement(message.readText())
    val messageTypeString = json.jsonObject["type"].toString().trim('"')
    val dataJson = json.jsonObject["data"]!!

    when (messageTypeString) {
        String::class.simpleName -> {
            ctrlProtocol.chat(Json.decodeFromJsonElement<String>(dataJson))
        }
        Point::class.simpleName -> {
            ctrlProtocol.point(Json.decodeFromJsonElement<Point>(dataJson))
        }
        GameStatus::class.simpleName -> {
            ctrlProtocol.event(Json.decodeFromJsonElement<GameStatus>(dataJson))
        }
    }
}

inline fun<reified T> sendCtrlProtocol(data: T) = Json.encodeToString(buildJsonObject {
    put("type", data!!::class.simpleName)
    put("data", Json.encodeToJsonElement<T>(data))
})

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
