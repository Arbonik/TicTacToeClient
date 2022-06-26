package com.ctrlya.tictactoe.network

import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import com.ctrlya.tictactoe.network.model.CreateRoomResponse
import com.ctrlya.tictactoe.network.model.RoomsResponse


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
}