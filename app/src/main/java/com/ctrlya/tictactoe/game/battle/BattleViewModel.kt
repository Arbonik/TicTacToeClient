package com.arbonik.tictactoebot.ui.game.battle

import android.graphics.Point
import androidx.lifecycle.viewModelScope
import com.arbonik.tictactoebot.players.BattleBot
import com.arbonik.tictactoebot.players.GUIPlayer
import com.arbonik.tictactoebot.core.Mark
import com.arbonik.tictactoebot.core.Party
import com.arbonik.tictactoebot.core.player.Player
import com.arbonik.tictactoebot.core.utils.BattlePartyController
import com.arbonik.tictactoebot.core.utils.BattlePartyStatusListener
import com.arbonik.tictactoebot.database.players.PlayerData
import com.arbonik.tictactoebot.database.players.PlayerType
import com.arbonik.tictactoebot.domain.usercase.GameInteractor
import com.arbonik.tictactoebot.domain.usercase.MemoryInteractor
import com.arbonik.tictactoebot.ui.game.base.GameViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BattleViewModel @Inject constructor(
    override val gameInteractor: GameInteractor,
    val memoryInteractor: MemoryInteractor
) : GameViewModel<BattlePartyController>() {

    private var _firstPlayer: Player? = null
    private var _secondPlayer: Player? = null

    override fun createPartyController(
        party: Party,
        player1: Player,
        player2: Player
    ): BattlePartyController {
        _firstPlayer = player1
        _secondPlayer = player2
        return BattlePartyController(
            party,
            player1,
            player2
        )
    }

    override fun onClick(point: Point) {
        super.onClick(point)
        _partyControllerInteractor?.let { interactor ->
            if (interactor.currentPlayer.value is GUIPlayer) {
                interactor.playerTurn(point, interactor.currentPlayer.value!!)
            }
        }
    }

    override fun playerFromData(playerData: PlayerData, mark: Mark): Player {
        if (playerData.type == PlayerType.BOT)
            return BattleBot(
                playerData.name,
                mark,
                playerData.id,
                memoryInteractor
            )
        else
            return super.playerFromData(playerData, mark)
    }

    override fun partyControllerCreated(partyController: BattlePartyController) {
        with(partyController) {
            //TODO null pointer !
            setCurrentPlayer(_firstPlayer!!)
            viewModelScope.launch {
                _firstPlayer?.attachToGame(partyController)
            }
            viewModelScope.launch {
                _secondPlayer?.attachToGame(partyController)
            }
            viewModelScope.launch {
                initPartyStatusListener(object : BattlePartyStatusListener {
                    override fun result(winner: Player) {
                        sendGameMessage(winner.name + " ПОБЕДИЛ!")
                    }

                    override fun draw() {
                        sendGameMessage("Ничья!")
                    }

                    override fun play(currentPlayer: Player) {
                        sendGameMessage("Ход игрока " + currentPlayer.name)
                    }

                    override fun start() {
                        sendGameMessage("Игра Началась!")
                    }
                })
            }
        }
    }

    override fun restartGame() {
        viewModelScope.launch {
            _startGame?.let { loadGame(it) }
        }
    }
}