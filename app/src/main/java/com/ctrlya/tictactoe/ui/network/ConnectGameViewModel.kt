package com.ctrlya.tictactoe.ui.network

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctrlya.tictactoe.core.data.Point
import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import com.ctrlya.tictactoe.network.CtrlProtocol
import com.ctrlya.tictactoe.network.GameStatus
import com.ctrlya.tictactoe.network.NetworkGameInteractor
import com.ctrlya.tictactoe.network.model.CreateRoomResponse
import com.ctrlya.tictactoe.network.model.RoomsResponse
import com.ctrlya.tictactoe.ui.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ConnectGameViewModel(
    val interactor: NetworkGameInteractor
) : ViewModel() {

    private val _roomsResponse: MutableStateFlow<Resource<RoomsResponse>> =
        MutableStateFlow(Resource.Success(RoomsResponse()))
    val rooms: StateFlow<Resource<RoomsResponse>> = _roomsResponse

    private val _roomId: MutableStateFlow<Resource<String>> =
        MutableStateFlow(Resource.Loading())
    val roomId: StateFlow<Resource<String>> = _roomId

    fun loadRooms() {
        _roomsResponse.value = Resource.Loading()
        viewModelScope.launch {
            _roomsResponse.value = try {
                Resource.Success(interactor.loadAllRooms())
            } catch (e: Exception) {
                Resource.Error(e.message.toString())
            }
        }
    }

    fun createRoom(
        gameSettings: BattlefieldSettings,
        callBack: (room: CreateRoomResponse?) -> Unit
    ) {
        viewModelScope.launch {
            val result = interactor.createRoom(gameSettings)
            callBack(result)
        }
    }

    fun matchMaking() {
        viewModelScope.launch {
            val freeRooms = interactor.freeRooms()
            if (freeRooms.items.isNullOrEmpty()) {
                val roomId = interactor.createRoom(BattlefieldSettings(3, 3, 3, false))
                if (roomId != null) {
                    _roomId.value = Resource.Success(roomId.roomId)
                }
            } else {
                val elem = freeRooms.items.find { item -> item.id != null }
                if (elem?.id != null) {
                    _roomId.value = Resource.Success(elem.id)
                } else {
                    _roomId.value = Resource.Error(":C")
                }
            }
        }
    }

    fun ws(id: String, sharedFlow: SharedFlow<Point>, callback: (point: Point) -> Unit) {
        viewModelScope.launch {
            interactor.ws(id, sharedFlow, object : CtrlProtocol {
                override suspend fun point(point: Point) {
                    Log.d("DQOFNQEFQEF", point.toString())
                    callback(point)
                }

                override suspend fun chat(message: String) {
                }

                override suspend fun event(event: GameStatus) {
                }
            })
        }
    }
}