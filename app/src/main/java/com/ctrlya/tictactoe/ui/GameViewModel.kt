package com.ctrlya.tictactoe.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctrlya.tictactoe.network.NetworkGameInteractor
import kotlinx.coroutines.launch

class GameViewModel(
    val networkGameInteractor: NetworkGameInteractor
) : ViewModel() {
    fun createRoom(){
        viewModelScope.launch {
            networkGameInteractor.a()
        }
    }
}