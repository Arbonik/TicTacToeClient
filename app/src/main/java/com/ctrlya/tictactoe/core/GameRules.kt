package com.arbonik.tictactoebot.core

import android.graphics.Point
import com.arbonik.tictactoebot.core.utils.Field
import com.arbonik.tictactoebot.core.utils.convertToString
import com.arbonik.tictactoebot.core.utils.height
import com.arbonik.tictactoebot.core.utils.width

class GameRules(
    private val symToWin: Int = 3
) {
    fun isWin(
        position: Point,
        marks: MutableList<MutableList<Mark>>
    ): Boolean {
        if (marks[position.y][position.x] == Mark.EMPTY) return false
        return horizontalSequence(position.x, marks[position.y]) >= symToWin ||
                horizontalSequence(position.y, transposeMatrix(marks)[position.x]) >= symToWin ||
                mainConuntDig(position, marks) >= symToWin ||
                twoCountDig(position, marks) >= symToWin
    }

    private fun horizontalSequence(
        x: Int,
        list: MutableList<Mark>
    ): Int {
        val str = list.convertToString()
        return maxSequenceLength(list[x].symbol, str)
    }

    private fun mainConuntDig(
        position: Point,
        marks: MutableList<MutableList<Mark>>
    ): Int {
        val str = mainDiagonalSequence(position, marks).convertToString()
        return maxSequenceLength(marks[position.y][position.x].symbol, str)
    }

    private fun twoCountDig(
        position: Point,
        marks: MutableList<MutableList<Mark>>
    ): Int {
        val str = twoDiagonalSequence(position, marks).convertToString()
        return maxSequenceLength(marks[position.y][position.x].symbol, str)
    }

    private fun mainDiagonalSequence(
        position: Point,
        marks: Field
    ): MutableList<Mark> {
        val diffD = position.x - position.y
        var result = mutableListOf<Mark>()
        for (i in 0..marks.width) {
            val x = i
            val y = x - diffD
            if (y in 0 until marks.height && x < marks.width) {
                result.add(marks[y][x])
            }
        }
        return result
    }

    private fun twoDiagonalSequence(
        position: Point,
        marks: Field
    ): MutableList<Mark> {
        val sumD = position.x + position.y
        var result = mutableListOf<Mark>()
        for (i in 0..marks.width) {
            val x = i
            val y = sumD - x
            if (y in 0 until marks.height && x < marks.width) {
                result.add(marks[y][x])
            }
        }
        return result
    }

    private fun transposeMatrix(
        marks: MutableList<MutableList<Mark>>
    ) = MutableList<MutableList<Mark>>(marks.first().size) {
        transposeCollumn(it, marks)
    }

    private fun <T> transposeCollumn(
        index: Int,
        marks: MutableList<MutableList<T>>
    ): MutableList<T> = MutableList(marks.size) {
        marks[it][index]
    }

    private fun maxSequenceLength(target: Char, str: String): Int {
        return str.split(
            Regex("[^$target]")
        )
            .maxOf {
                it.length
            }
    }

}