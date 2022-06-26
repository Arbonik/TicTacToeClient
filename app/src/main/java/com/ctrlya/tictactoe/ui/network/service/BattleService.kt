package com.ctrlya.tictactoe.ui.network.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.ctrlya.tictactoe.R
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class BattleService() : Service()
{
    private var webSocketSession: DefaultClientWebSocketSession? = null

    private val wakeLock: PowerManager.WakeLock  by lazy {
        (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
            newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "${getString(R.string.app_name)}::WakelockTag")
        }
    }
    private val networkCallback = object: ConnectivityManager.NetworkCallback()
    {
        // сеть доступна для использования
        override fun onAvailable(network: Network) {
            Log.d("network", "is on")
            super.onAvailable(network)
        }

        // соединение прервано
        override fun onLost(network: Network) {
            Log.d("network", "is off")
            super.onLost(network)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int
    {
        val notification = NotificationNetwork.track(this)
        NotificationManagerCompat
            .from(this)
            .notify(4,notification)
        startForeground(4, notification)

        wakeLock.acquire() // запрещаю переходить в спящий режим


        val connectivityManager = ContextCompat.getSystemService(applicationContext, ConnectivityManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager!!.registerDefaultNetworkCallback(networkCallback)
        } else {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
            connectivityManager!!.registerNetworkCallback(request, networkCallback)
        }
        return super.onStartCommand(intent, flags, startId)
    }
    private fun stopWebSocket()
    {
        MainScope().launch(Dispatchers.IO) {
            webSocketSession?.close(CloseReason(CloseReason.Codes.NORMAL, "end game"))
        }
    }


    private suspend fun startWebSocket(roomId: String)
    {
        MainScope().launch(Dispatchers.IO) {
            Repository.websocketClient().webSocket(
                method = HttpMethod.Get,
                host = "",
                path = r0oomId
            )
            {
                webSocketSession = this@webSocket


            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        wakeLock.release() // разрешаю переходить в спящий режим
        super.onDestroy()
    }
}