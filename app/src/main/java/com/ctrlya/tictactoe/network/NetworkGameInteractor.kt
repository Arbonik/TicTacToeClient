package com.ctrlya.tictactoe.network

import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import com.ctrlya.tictactoe.network.model.CreateRoomResponse
import com.ctrlya.tictactoe.network.model.RoomsResponse


class NetworkGameInteractor(
    private val ktorClient: TicTacToeClient
) {
    suspend fun createRoom(gameSettings: BattlefieldSettings): CreateRoomResponse? // isSuccess
    {
        return ktorClient.createRoom(gameSettings)
    }

    suspend fun loadAllRooms(): RoomsResponse {
        ktorClient.allRooms().onSuccess {
            return it
        }.onFailure {
        }
        return RoomsResponse()
    }
}