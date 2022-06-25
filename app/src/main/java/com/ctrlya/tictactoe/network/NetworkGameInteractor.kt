package com.ctrlya.tictactoe.network

import android.util.Log
import com.ctrlya.tictactoe.network.model.RoomsResponse


class NetworkGameInteractor(
    private val ktorClient: TicTacToeClient
) {
    suspend fun createRoom() {
        ktorClient.createRoom().onSuccess {
            Log.d("NETWORK_REQUEST", "SUCCESS")
        }.onFailure {
            Log.d("NETWORK_REQUEST", it.message.toString())
        }
    }

    suspend fun loadAllRooms(): RoomsResponse {
        ktorClient.allRooms().onSuccess {
            return it
        }.onFailure {
        }
        return RoomsResponse()
    }
}