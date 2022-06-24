package com.arbonik.tictactoebot.core

import android.graphics.Point
import com.arbonik.tictactoebot.core.utils.height
import com.arbonik.tictactoebot.core.utils.width
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class Battlefield(
    private val max_width: Int = 10,
    private val max_height: Int = 10,
    private val gameRuler: GameRules = GameRules(5)
) {
    private val _battlefieldState: MutableStateFlow<BattlefieldState> =
        MutableStateFlow(BattlefieldState.START)
    val battlefieldState : StateFlow<BattlefieldState> = _battlefieldState

    val marks = MutableList<MutableList<Mark>>(1) {
        mutableListOf(Mark.EMPTY)
    }
    val height
        get() = marks.height
    val width
        get() = marks.width

    var prevMove = Point()

    fun addMark(x: Int, y: Int, mark: Mark) {
        marks[y][x] = mark
        prevMove = Point(x, y)
        update()
    }

    fun addMark(point: Point, mark: Mark) {
        addMark(point.x, point.y, mark)
    }

    fun restart(){
        marks.clear()
        marks.add(mutableListOf(Mark.EMPTY))
        _battlefieldState.value = BattlefieldState.START
    }

    private fun update() {
        autoResize()
        _battlefieldState.value = battlefieldState()
    }

    private fun battlefieldState(): BattlefieldState {
        return when {
            isWin() -> BattlefieldState.WIN
            isDraw() -> BattlefieldState.DRAW
            else -> BattlefieldState.PLAY
        }
    }

    //TODO can increase algorithm
    private fun isDraw(): Boolean = marks.find { it.contains(Mark.EMPTY) } == null

    private fun isWin(): Boolean {
        return gameRuler.isWin(prevMove, marks)
    }

    private fun autoResize() {
        if (marks.size < max_height) {
            val upBorder = marks.first().find { it != Mark.EMPTY }
            if (upBorder != null) {
                marks.add(0, MutableList(marks.first().size) { Mark.EMPTY })
            }

            val bottomBorder = marks.last().find { it != Mark.EMPTY }
            if (bottomBorder != null) {
                marks.add(MutableList(marks.last().size) { Mark.EMPTY })
            }
        }

        if (marks.first().size < max_width) {
            val leftBorder = marks.find { it.first() != Mark.EMPTY }
            if (leftBorder != null) {
                for (list in marks)
                    list.add(0, Mark.EMPTY)
            }

            val rightBorder = marks.find { it.last() != Mark.EMPTY }
            if (rightBorder != null) {
                for (list in marks)
                    list.add(Mark.EMPTY)
            }
        }
    }
}