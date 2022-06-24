package com.arbonik.tictactoebot.ui.game.base

import android.graphics.Point
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arbonik.tictactoebot.players.GUIPlayer
import com.arbonik.tictactoebot.core.GameRules
import com.arbonik.tictactoebot.core.Mark
import com.arbonik.tictactoebot.core.Party
import com.arbonik.tictactoebot.core.player.Player
import com.arbonik.tictactoebot.core.utils.PartyController
import com.arbonik.tictactoebot.database.game.Game
import com.arbonik.tictactoebot.database.game.GameConfig
import com.arbonik.tictactoebot.database.players.PlayerData
import com.arbonik.tictactoebot.database.players.PlayerType
import com.arbonik.tictactoebot.domain.usercase.GameInteractor
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
/*
* Lifecycle GameViewModel:
* 1. loadGame(game:Game) - init
* 2 createInteractor
* 3 interactor created*/
abstract class GameViewModel<T : PartyController> : ViewModel() {

    abstract val gameInteractor: GameInteractor
    protected var _partyControllerInteractor: T? = null
        private set
    protected var _startGame :Game? = null

    private val _field: MutableStateFlow<Array<Array<Mark>>> =
        MutableStateFlow(arrayOf(arrayOf(Mark.EMPTY)))
    val field: StateFlow<Array<Array<Mark>>> = _field

    private var _party: Party? = null
        private set(value) {
            field = value
            viewModelScope.launch {
                value?.field?.collect {
                    _field.value = it
                }
            }
        }

    private val _gameMessages: MutableStateFlow<String?> = MutableStateFlow(null)
    val gameMessages: StateFlow<String?> = _gameMessages


    fun loadGame(game: Game) {
        viewModelScope.launch {
            _startGame = game
            val gameId = gameInteractor.createGame(game)

            val gameLoad = gameInteractor.loadGame(gameId)

            val gameData = gameDataLoader(gameLoad)

            val partyWithPlayrs = gameLoaded(
                gameData.first.await(),
                gameData.second.await(),
                gameData.third.await()
            )
            _party = partyWithPlayrs.first

            val partyControlled = createPartyController(
                partyWithPlayrs.first,
                partyWithPlayrs.second,
                partyWithPlayrs.third
            )
            _partyControllerInteractor = partyControlled
            partyControllerCreated(partyControlled)
        }
    }

    protected suspend fun createGame(game: Game) = gameInteractor.createGame(game)

    protected open suspend fun firstPlayerDataLoader(playerId: Long): PlayerData =
        gameInteractor.loadPlayer(playerId)

    protected open suspend fun secondPlayerDataLoader(playerId: Long): PlayerData =
        gameInteractor.loadPlayer(playerId)


    protected suspend fun gameDataLoader(game: Game) =
        withContext(viewModelScope.coroutineContext) {
            val config = async { gameInteractor.loadConfig(game.gameConfig) }
            val firstPlayerData = async { firstPlayerDataLoader(game.firstPlayerId) }
            val secondPlayerData = async { secondPlayerDataLoader(game.secondPlayerId) }
            Triple(firstPlayerData, secondPlayerData, config)
        }

    abstract fun createPartyController(
        party: Party,
        player1: Player,
        player2: Player
    ): T

    abstract fun partyControllerCreated(
        partyController: T
    )

    abstract fun restartGame()

    open fun onClick(point: Point) {
    }

    protected fun sendGameMessage(message: String) {
        _gameMessages.value = message
    }

    protected open fun playerFromData(playerData: PlayerData, mark: Mark): Player {
        return when (playerData.type) {
            PlayerType.REAL -> GUIPlayer(playerData.name, mark)
            else -> GUIPlayer(playerData.name, mark)
        }
    }

    private fun gameLoaded(
        player1: PlayerData,
        player2: PlayerData,
        gameConfig: GameConfig
    ): Triple<Party, Player, Player> {
        val firstPlayer = playerFromData(player1, Mark.X)
        val secondPlayer = playerFromData(player2, Mark.O)

        val party = Party.instance(
            gameConfig.width,
            gameConfig.height,
            GameRules(gameConfig.symToWin)
        )
        return Triple(party, firstPlayer, secondPlayer)
    }

}