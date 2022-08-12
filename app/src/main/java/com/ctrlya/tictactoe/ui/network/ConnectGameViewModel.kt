package com.ctrlya.tictactoe.ui.network

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.data.Point
import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import com.ctrlya.tictactoe.network.CtrlProtocol
import com.ctrlya.tictactoe.network.GameResult
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

    private var i = 0
    private val currentMark : Mark
        get() = if (i++ % 2 == 0) Mark.X else Mark.O


    // временное решение, требуется перенос в другое место
    val field = MutableList(11){
        MutableList(11){
            Mark.EMPTY
        }
    }

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

    fun matchMaking()
    {
        viewModelScope.launch {
            _roomId.value = Resource.Loading("Поиск свободной комнаты")
            val freeRooms = interactor.freeRooms()
            if (freeRooms.items.isNullOrEmpty()) {
                _roomId.value = Resource.Loading("Создание комнаты")
                val roomId = interactor.createRoom(BattlefieldSettings(3, 3, 3, false))
                _roomId.value = Resource.Loading("Подключение")
                if (roomId != null) {
                    _roomId.value = Resource.Success(roomId.roomId)
                }
            } else {
                _roomId.value = Resource.Loading("Подключение")
                val elem = freeRooms.items.find { item -> item.id != null }
                if (elem?.id != null) {
                    _roomId.value = Resource.Success(elem.id)
                } else {
                    _roomId.value = Resource.Error(":C")
                }
            }
        }
    }

    private val _info: MutableStateFlow<String> = MutableStateFlow("")
    val info: StateFlow<String> = _info

    fun ws(
        id: String,
        sharedFlow: SharedFlow<Point>,
        callback: (point: Point) -> Unit
    ){
        viewModelScope.launch {
            interactor.ws(id, sharedFlow, object : CtrlProtocol {
                override suspend fun point(point: Point) {
                    Log.d("DQOFNQEFQEF", point.toString())
                    field[point.y][point.x] = currentMark
                    callback(point)
                }

                override suspend fun info(message: String) {
                    _info.value = message
                }

                override suspend fun result(event: GameResult) {
                }
            })
        }
    }
}