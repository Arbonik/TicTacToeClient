package com.ctrlya.tictactoe.core.player

import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.data.Point
import com.ctrlya.tictactoe.core.data.toHash
import com.ctrlya.tictactoe.core.game.GameEvent
import com.ctrlya.tictactoe.core.game.GameService
import com.ctrlya.tictactoe.database.memory.MemoryInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest

class SkilledBotPlayer(
    override val mark: Mark,
    val memoryInteractor: MemoryInteractor,
    val coroutineScope: CoroutineScope
) : Player {

    private val mutableSharedFlow: MutableSharedFlow<Point> = MutableSharedFlow()

    private var memory : LinkedHashMap<String, Point> = linkedMapOf()

    override suspend fun connectToGame(game: GameService) {
        memory = memoryInteractor.memorySnupshot(LearnBot.HOBOT)
        game.gameStatusFlow.collectLatest {  event ->
            when (event){
                is GameEvent.Turn -> {
                    if (event.player != this){
                        val point = memory.getOrElse(game.battlefieldStateFlow.value.toHash()){
                            throw Exception()
                        }
                        mutableSharedFlow.emit(point)
                    }
                }
            }
        }
    }

    override suspend fun turn(): Flow<Point> = mutableSharedFlow
}