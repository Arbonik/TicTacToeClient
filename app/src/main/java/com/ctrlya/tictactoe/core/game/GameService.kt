package com.ctrlya.tictactoe.core.game

import com.ctrlya.tictactoe.core.data.Point
import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import com.ctrlya.tictactoe.core.party.BaseParty
import com.ctrlya.tictactoe.core.player.Player
import domain.game.GameStartedStatus

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// TODO need id to game
open class GameService(
    settings: BattlefieldSettings,
    private val coroutineScope: CoroutineScope
) : BaseParty(
    settings
) {
    private val _gameStatusFlow: MutableSharedFlow<GameProgress> = MutableStateFlow(GameProgress.CREATED)
    val gameStatusFlow = _gameStatusFlow.asSharedFlow()

    private val judge = Judge()

    var firstPlayer: Player? = null
        private set
    var secondPlayer: Player? = null
        private set
    var currentPlayer: Player? = null
        private set

    fun isCanStarted(): GameStartedStatus {
        return when {
            firstPlayer == null && secondPlayer == null -> GameStartedStatus.NO_PLAYERS
            firstPlayer == null -> GameStartedStatus.NO_FIRST_PLAYER
            secondPlayer == null -> GameStartedStatus.NO_SECOND_PLAYER
            currentPlayer == null -> GameStartedStatus.NO_CURRENT_PLAYERS
            else -> { GameStartedStatus.SUCCESS }
        }
    }

    fun setPlayer(first: Player? = null, second: Player? = null, current: Player? = null) {
        first?.let {
            firstPlayer = it
        }
        second?.let {
            secondPlayer = it
        }
        current?.let { currentPlayer = it }
    }

    fun start() {
        if (isCanStarted() == GameStartedStatus.SUCCESS) {
            updateProgress(
                GameProgress.Start(currentPlayer!!)
            )
        }
    }

    fun playerTurn(player: Player, position: Point) {
        if (player == currentPlayer) {
            turn(position, player.mark)
            updateProgress(GameProgress.Turn(player, position))
            fixedResultGame(position, player)
            swapCurrentPlayer()
        }
    }

    private fun fixedResultGame(position: Point, player: Player) {
        if (judge.isWin(settings.winSequenceLength, position, _marks)) {
            updateProgress(GameProgress.Win(player))
        }else if (judge.isDraw(_marks)) {
            updateProgress(GameProgress.DRAW)
        }
    }

    private fun swapCurrentPlayer() {
        currentPlayer = if (currentPlayer == firstPlayer)
            secondPlayer
        else
            firstPlayer
    }

    fun end() {
        //FIXME CAN CANCEL ALL CHILDREN, MORE ONE GAME
        coroutineScope.cancel()
        updateProgress(GameProgress.END)
    }

    private fun updateProgress(gameProgress : GameProgress) {
        coroutineScope.launch {
            _gameStatusFlow.emit(gameProgress)
        }
    }
}