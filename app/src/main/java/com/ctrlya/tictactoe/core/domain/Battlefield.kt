package com.ctrlya.tictactoe.core.domain

import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.data.Point

// отвечает за инициализацию, перестроение поля и добавление на него маркеров
open class Battlefield(
    val settings: BattlefieldSettings
) {
    private val _maxWidth: Int
        get() = settings.maxWidth
    private val _maxHeight: Int
        get() = settings.maxHeight

    protected val _marks: MutableList<MutableList<Mark>> = if (settings.resizable) {
        MutableList<MutableList<Mark>>(1) {
            mutableListOf(Mark.EMPTY)
        }
    } else {
        MutableList<MutableList<Mark>>(_maxHeight) {
            MutableList(_maxWidth) {
                Mark.EMPTY
            }
        }
    }
    val marks: List<List<Mark>>
        get() = _marks.map { it.toList() }.toList()

    fun addMark(x: Int, y: Int, mark: Mark) {
        _marks[y][x] = mark
        resize()
    }

    fun addMark(point: Point, mark: Mark) {
        addMark(point.x, point.y, mark)
    }

    fun restart(): Battlefield = Battlefield(settings)

    private fun resize() {
        if (settings.resizable) {
            verticalResize()
            horizontalResize()
        }
    }

    private fun horizontalResize() {
        if (_marks.first().size < _maxWidth) {
            val leftBorder = _marks.find { it.first() != Mark.EMPTY }
            if (leftBorder != null) {
                for (list in _marks)
                    list.add(0, Mark.EMPTY)
            }
            val rightBorder = _marks.find { it.last() != Mark.EMPTY }
            if (rightBorder != null) {
                for (list in _marks)
                    list.add(Mark.EMPTY)
            }
        }
    }

    private fun verticalResize() {
        if (_marks.size < _maxHeight) {
            val upBorder = _marks.first().find { it != Mark.EMPTY }
            if (upBorder != null) {
                _marks.add(0, MutableList(_marks.first().size) { Mark.EMPTY })
            }
            val bottomBorder = _marks.last().find { it != Mark.EMPTY }
            if (bottomBorder != null) {
                _marks.add(MutableList(_marks.last().size) { Mark.EMPTY })
            }
        }
    }
}