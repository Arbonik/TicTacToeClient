package com.arbonik.tictactoebot.core

import android.graphics.Point
import com.arbonik.tictactoebot.core.utils.height
import com.arbonik.tictactoebot.core.utils.width
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class Party {
    companion object {
        fun instance(
            maxWidth: Int,
            maxHeight: Int,
            gameRules: GameRules
        ) = Party().apply {
            battlefield = Battlefield(
                maxWidth,
                maxHeight,
                gameRules
            )
        }

        private val defaultFieldValue = arrayOf(arrayOf(Mark.EMPTY))
    }

    var battlefield: Battlefield = Battlefield()
        private set

    private val _field: MutableStateFlow<Array<Array<Mark>>> =
        MutableStateFlow(defaultFieldValue)
    val field: StateFlow<Array<Array<Mark>>> = _field

    private val _partyStatus: MutableStateFlow<PartyState> = MutableStateFlow(PartyState.ON_START)
    val partyStatus: StateFlow<PartyState> = _partyStatus

    fun restart() {
        battlefield.restart()
        _field.value = defaultFieldValue
        _partyStatus.value = PartyState.ON_START
    }

    fun turn(x: Int, y: Int, mark: Mark) {
        battlefield.addMark(
            x,
            y,
            mark
        )
        //TODO bad perfomance
        _field.value = Array(battlefield.marks.height) { y ->
            Array(battlefield.marks.width) { x ->
                battlefield.marks[y][x]
            }
        }
        updateGameStatus()
    }

    fun turn(point: Point, mark: Mark) {
        turn(point.x, point.y, mark)
    }

    private fun updateGameStatus() {
        when (battlefield.battlefieldState.value) {
            BattlefieldState.WIN -> {
                _partyStatus.value = PartyState.RESULT()
            }
            BattlefieldState.DRAW -> {
                _partyStatus.value = PartyState.DRAW
            }
            BattlefieldState.PLAY -> {
                _partyStatus.value = PartyState.PLAY(battlefield.prevMove)
            }
            BattlefieldState.START -> PartyState.ON_START
        }
    }
}
