package com.ctrlya.tictactoe.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import com.ctrlya.tictactoe.network.NetworkGameInteractor
import com.ctrlya.tictactoe.network.model.CreateRoomResponse
import kotlinx.coroutines.launch

class CreateRoomViewModel(
    val interactor: NetworkGameInteractor
): ViewModel()
{
    fun createRoom(
        gameSetting: BattlefieldSettings,
        callback: (createRoomResponse: CreateRoomResponse?) -> Unit
    )
    {
        viewModelScope.launch {
            callback(
                interactor.createRoom(
                    gameSetting
                )
            )
        }


    }
}