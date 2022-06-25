package com.ctrlya.tictactoe.network

import android.util.Log


class NetworkGameInteractor(
    private val ktorClient: TicTacToeClient
) {
    suspend fun a(){
       ktorClient.createRoom().onSuccess {
           Log.d("NETWORK_REQUEST", "SUCCESS")
       }.onFailure {
           Log.d("NETWORK_REQUEST", it.message.toString())
       }
    }
}