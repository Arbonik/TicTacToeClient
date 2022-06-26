package com.ctrlya.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import com.ctrlya.tictactoe.network.TicTacToeClient
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val ktorClient: TicTacToeClient by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MainScope().launch(Dispatchers.IO) {
//            ktorClient.client.webSocket(method = HttpMethod.Get, host = "mysterious-tor-86270.herokuapp.com", path = "/8f1c9ba8-203e-4035-a8da-4fbb538c5242") {
//                for (frame in incoming){
//                    if (frame is Frame.Text)
//                        Log.d("DQOFNQEFQEF", frame.readText())
//                }
//            }
        }
    }
}