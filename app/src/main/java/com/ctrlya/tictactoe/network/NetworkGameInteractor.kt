package com.ctrlya.tictactoe.network

import com.ctrlya.tictactoe.core.data.Point
import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import com.ctrlya.tictactoe.network.model.CreateRoomResponse
import com.ctrlya.tictactoe.network.model.RoomsResponse
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest


class NetworkGameInteractor(
    private val ktorClient: TicTacToeClient
) {
    suspend fun createRoom(gameSettings: BattlefieldSettings): CreateRoomResponse? {
        val result =  ktorClient.createRoom(gameSettings)
        return result.getOrNull()
    }

    suspend fun loadAllRooms(): RoomsResponse {
        val result = ktorClient.allRooms()
        return result.getOrDefault(RoomsResponse())
    }
    suspend fun freeRooms(): RoomsResponse {
        val result = ktorClient.freeRooms()
        return result.getOrDefault(RoomsResponse())
    }

    suspend fun ws(id:String, sharedFlow: SharedFlow<Point>, ctrlProtocol: CtrlProtocol)/*: ctrlProtocol: CtrlProtocol,*/{
        ktorClient.connectToGame(id, sharedFlow).collectLatest {
            it.messageReceive(ctrlProtocol)
        }
    }
}