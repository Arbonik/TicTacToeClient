package com.ctrlya.tictactoe.ui.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import com.ctrlya.tictactoe.network.NetworkGameInteractor
import com.ctrlya.tictactoe.network.model.CreateRoomResponse
import com.ctrlya.tictactoe.network.model.RoomsResponse
import com.ctrlya.tictactoe.ui.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConnectGameViewModel(
    val interactor: NetworkGameInteractor
) : ViewModel() {

    private val _roomsResponse: MutableStateFlow<Resource<RoomsResponse>> =
        MutableStateFlow(Resource.Success(RoomsResponse()))
    val rooms: StateFlow<Resource<RoomsResponse>> = _roomsResponse

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

    fun createRoom(gameSettings: BattlefieldSettings, callBack: (room: CreateRoomResponse?) -> Unit) {
        viewModelScope.launch {
            val result = interactor.createRoom(gameSettings)
            callBack(result)
        }
    }
}