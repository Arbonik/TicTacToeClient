package com.ctrlya.tictactoe.core.game

import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.data.Point

open class Judge {

    fun isWin(
        symToWin: Int,
        position: Point,
        marks: List<List<Mark>>
    ): Boolean {
        if (marks[position.y][position.x] == Mark.EMPTY) return false
        return horizontalSequence(position.x, marks[position.y]) >= symToWin ||
                horizontalSequence(position.y, transposeMatrix(marks)[position.x]) >= symToWin ||
                mainConuntDig(position, marks) >= symToWin ||
                twoCountDig(position, marks) >= symToWin
    }

    fun isDraw(marks: List<List<Mark>>): Boolean = marks.find { it.contains(Mark.EMPTY) } == null

    private fun horizontalSequence(
        x: Int,
        list: List<Mark>
    ): Int {
        val str = list.joinToString("")
        return maxSequenceLength(list[x].code, str)
    }

    private fun mainConuntDig(
        position: Point,
        marks: List<List<Mark>>
    ): Int {
        val str = mainDiagonalSequence(position, marks).joinToString("")
        return maxSequenceLength(marks[position.y][position.x].code, str)
    }

    private fun twoCountDig(
        position: Point,
        marks: List<List<Mark>>
    ): Int {
        val str = twoDiagonalSequence(position, marks).joinToString("")
        return maxSequenceLength(marks[position.y][position.x].code, str)
    }

    private fun <T> mainDiagonalSequence(
        position: Point,
        marks: List<List<T>>
    ): List<T> {
        if (marks.isEmpty()) return mutableListOf()
        val diffD = position.x - position.y
        val result = mutableListOf<T>()
        for (i in 0..marks.first().size) {
            val x = i
            val y = x - diffD
            if (y in marks.indices && x < marks.first().size) {
                result.add(marks[y][x])
            }
        }
        return result
    }

    private fun <T> twoDiagonalSequence(
        position: Point,
        marks: List<List<T>>
    ): List<T> {
        if (marks.isEmpty()) return mutableListOf()
        val sumD = position.x + position.y
        val result = mutableListOf<T>()
        for (i in 0..marks.first().size) {
            val x = i
            val y = sumD - x
            if (y in marks.indices && x < marks.first().size) {
                result.add(marks[y][x])
            }
        }
        return result
    }

    private fun <T> transposeMatrix(
        marks: List<List<T>>
    ) = List<List<T>>(marks.first().size) {
        transposeColumn(it, marks)
    }

    private fun <T> transposeColumn(
        index: Int,
        marks: List<List<T>>
    ): List<T> = List(marks.size) {
        marks[it][index]
    }

    private fun <T> maxSequenceLength(target: T, str: String): Int {
        return str.split(Regex("[^${target.toString()}]"))
            .maxOf {
                it.length
            }
    }
}