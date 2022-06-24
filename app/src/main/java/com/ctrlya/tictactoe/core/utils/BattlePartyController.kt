package com.arbonik.tictactoebot.core.utils

import android.graphics.Point
import com.arbonik.tictactoebot.core.Party
import com.arbonik.tictactoebot.core.PartyState
import com.arbonik.tictactoebot.core.TurnValidateStatus
import com.arbonik.tictactoebot.core.player.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect

interface BattlePartyStatusListener {
    fun result(winner: Player)
    fun draw()
    fun play(currentPlayer: Player)
    fun start()
}

open class BattlePartyController(
    override val party: Party,
    private val firstPlayer: Player,
    private val secondPlayer: Player,
) : PartyController() {

    private val _currentPlayer: MutableStateFlow<Player?> =
        MutableStateFlow(null)
    val currentPlayer: StateFlow<Player?> = _currentPlayer

    fun setCurrentPlayer(player: Player) {
        if (player == firstPlayer || player == secondPlayer)
            _currentPlayer.value = player
        else throw Exception("CurrentPlayerMustBeRegistrationOnGame")
    }

    fun playerTurn(point: Point, player: Player) {
        if (_currentPlayer.value == player && validateTurn(point) == TurnValidateStatus.SUCCESS) {
            turn(point, player.mark)
        }
    }

    suspend fun initPartyStatusListener(battlePartyStatusListener: BattlePartyStatusListener) {
        party.partyStatus.collect { state ->
            when (state) {
                PartyState.DRAW -> {
                    battlePartyStatusListener.draw()
                }
                PartyState.ON_START -> {
                    battlePartyStatusListener.start()
                }
                is PartyState.PLAY -> {
                    swapTurn()
                    _currentPlayer.value?.let { player -> battlePartyStatusListener.play(player) }
                }
                is PartyState.RESULT -> {
                    _currentPlayer.value?.let { player -> battlePartyStatusListener.result(player) }
                }
            }
        }
    }

    private fun swapTurn() {
        _currentPlayer.value = if (_currentPlayer.value == firstPlayer)
            secondPlayer
        else firstPlayer
    }
}