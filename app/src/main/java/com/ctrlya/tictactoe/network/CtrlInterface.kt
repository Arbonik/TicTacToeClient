package com.ctrlya.tictactoe.network

import com.ctrlya.tictactoe.core.data.Point
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*

interface CtrlProtocol {
    suspend fun point(point: Point)
    suspend fun info(message: String)
    suspend fun result(event: GameResult)
}

//метод, обрабатывающий полученное сообщение с помощью ctrProtocol коллбека
suspend fun Frame.Text.messageReceive(ctrlProtocol: CtrlProtocol) {
    val json = Json.parseToJsonElement(this.readText())
    val messageTypeString = json.jsonObject["type"].toString().trim('"')
    val dataJson = json.jsonObject["data"]!!

    when (messageTypeString) {
        String::class.simpleName -> {
            ctrlProtocol.info(Json.decodeFromJsonElement<String>(dataJson))
        }
        Point::class.simpleName -> {
            ctrlProtocol.point(Json.decodeFromJsonElement<Point>(dataJson))
        }
        GameResult::class.simpleName -> {
            ctrlProtocol.result(Json.decodeFromJsonElement<GameResult>(dataJson))
        }
    }
}

// метод, приводящий сообщение к ctrlprotocol стандарту
inline fun <reified T> sendCtrlProtocol(data: T) = Json.encodeToString(buildJsonObject {
    put("type", data!!::class.simpleName)
    put("data", Json.encodeToJsonElement<T>(data))
})

enum class Choose {
    Rock,
    Paper,
    Scissors
}

enum class GameResult {
    Win,
    Lose,
    Draw
}


/**
Init - значение при создании,
Created - комната была создана и настроена,
Started - оба игрока подключились
InProgress - оба игрока подключились
finish - игра закончена
 **/
enum class GameStatus {
    Init,
    Created,
    Started,
    Progress,
    Finish,
}
