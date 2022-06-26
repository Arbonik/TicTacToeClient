package com.ctrlya.tictactoe.core.game

import android.util.Log
import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.data.Point
import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import com.ctrlya.tictactoe.core.party.BaseParty
import com.ctrlya.tictactoe.core.party.TurnStatus
import com.ctrlya.tictactoe.core.player.Player
import domain.game.GameStartedStatus

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// TODO need id to game
open class GameService(
    settings: BattlefieldSettings,
    protected val coroutineScope: CoroutineScope
) : BaseParty(
    settings
) {
    private val _gameStatusFlow: MutableSharedFlow<GameEvent> = MutableStateFlow(GameEvent.CREATED)
    val _gameWinLine: MutableStateFlow<WinLine?> = MutableStateFlow(null)
    val gameStatusFlow = _gameStatusFlow.asSharedFlow()

    private val judge = Judge()

    open var firstPlayer: Player? = null
        protected set
    open var secondPlayer: Player? = null
        protected set
    open var currentPlayer: Player? = null
        protected set

    fun isCanStarted(): GameStartedStatus {
        return when {
            firstPlayer == null && secondPlayer == null -> GameStartedStatus.NO_PLAYERS
            firstPlayer == null -> GameStartedStatus.NO_FIRST_PLAYER
            secondPlayer == null -> GameStartedStatus.NO_SECOND_PLAYER
            currentPlayer == null -> GameStartedStatus.NO_CURRENT_PLAYERS
            else -> {
                GameStartedStatus.SUCCESS
            }
        }
    }

    fun setPlayer(first: Player? = null, second: Player? = null, current: Player? = null) {
        first?.let { player ->
            firstPlayer = player
            coroutineScope.launch {
                player.turn().collectLatest { point ->
                    Log.d("FIELD_UPDATE", "1")
                    playerTurn(player, point/*, player.mark*/)
                }
            }
        }
        second?.let { player ->
            secondPlayer = player
            coroutineScope.launch {
                player.turn().collectLatest { point ->
                    Log.d("FIELD_UPDATE", "2")
                    playerTurn(player, point/*, player.mark*/)
                }
            }
        }
        current?.let { currentPlayer = it }
    }

    open fun start() {
        if (isCanStarted() == GameStartedStatus.SUCCESS) {
            updateProgress(
                GameEvent.Start(currentPlayer!!)
            )
        }
    }

    fun playerTurn(player: Player, position: Point, mark : Mark = player.mark) {
        if (player == currentPlayer) {
            if (turn(position, mark) == TurnStatus.SUCCESS) {
                updateProgress(GameEvent.Turn(player, position))
                fixedResultGame(position, player)
                swapCurrentPlayer()
            }
        }
    }

    private fun fixedResultGame(position: Point, player: Player) {
        if (judge.isWin(settings.winSequenceLength, position, _marks)) {
            updateProgress(GameEvent.Win(player))
            cancelScope()
        } else if (judge.isDraw(_marks)) {
            updateProgress(GameEvent.DRAW)
            cancelScope()
        }
    }

    private fun cancelScope() {
        coroutineScope.launch {
            delay(50)
            coroutineScope.cancel()
        }
    }

    fun searchWinLine(marks: List<List<Mark>>, position: Point) {
        settings.winSequenceLength

    }

    protected fun swapCurrentPlayer() {
        currentPlayer = if (currentPlayer == firstPlayer)
            secondPlayer
        else
            firstPlayer
    }

    fun end() {
        //FIXME CAN CANCEL ALL CHILDREN, MORE ONE GAME
        coroutineScope.cancel()
        updateProgress(GameEvent.END)
    }

    fun sendMessage(message: String) {
        updateProgress(GameEvent.Message(message))
    }

    private fun updateProgress(gameEvent: GameEvent) {
        coroutineScope.launch {
            _gameStatusFlow.emit(gameEvent)
        }
    }
}