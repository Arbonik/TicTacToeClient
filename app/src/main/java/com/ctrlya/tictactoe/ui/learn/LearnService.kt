package com.ctrlya.tictactoe.ui.learn

import android.util.Log
import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.data.toHash
import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import com.ctrlya.tictactoe.core.game.GameService
import com.ctrlya.tictactoe.core.player.LearnBot
import com.ctrlya.tictactoe.core.player.Player
import com.ctrlya.tictactoe.core.player.RealPlayer
import com.ctrlya.tictactoe.database.memory.MemoryInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LearnService(
    settings: BattlefieldSettings,
    coroutineScope: CoroutineScope,
    memoryInteractor: MemoryInteractor
) : GameService(settings, coroutineScope) {

    private var learnPosition: Array<Array<Mark>> = arrayOf()
    private val randomMarks = listOf(Mark.X, Mark.O).shuffled()
    val teatcher = RealPlayer(randomMarks.last(), coroutineScope)
    override var firstPlayer: Player? = teatcher
    val student = LearnBot(randomMarks.first(), memoryInteractor, coroutineScope)
    override var secondPlayer: Player? = student
    override var currentPlayer: Player? = listOf(teatcher, student).random()

    private var saveNextTurn: Boolean = false

    fun learnBotTurn() {
        val hash = battlefieldStateFlow.value.toHash()
        Log.d("HASH", hash)
        val array = battlefieldStateFlow.value.map {
            it.toTypedArray()
        }.toTypedArray()
        if (student.isFamiliarPosition(hash)) {
            playerTurn(student, student.turn(array))
        } else {
            sendMessage("Я не знаю, куда ходить :с, подскажи?")
            learnPosition = array
            saveNextTurn = true
        }
    }

    override fun start() {
        coroutineScope.launch {
            teatcher.turn().collectLatest { point ->
                playerTurn(currentPlayer!!, point, currentPlayer!!.mark)
                if (saveNextTurn) {
                    student.learn(point, learnPosition)
                    saveNextTurn = false
                }
                learnBotTurn()
            }
        }
        coroutineScope.launch {
            student.fastMemoryUpdate()
        }
        super.start()
    }
}