package com.ctrlya.tictactoe

import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.data.Point
import com.ctrlya.tictactoe.core.game.WinLine
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleUnitTest {
    val pole = listOf(
        listOf<Mark>(Mark.X, Mark.EMPTY, Mark.X, Mark.X, Mark.X),
        listOf<Mark>(Mark.O, Mark.O, Mark.X, Mark.O, Mark.X),
        listOf<Mark>(Mark.EMPTY, Mark.EMPTY, Mark.X, Mark.O, Mark.O),
        listOf<Mark>(Mark.X, Mark.O, Mark.X, Mark.X, Mark.X),
        listOf<Mark>(Mark.EMPTY, Mark.EMPTY, Mark.X, Mark.O, Mark.X),
    )

    val position = Point(4,3)
    val result = WinLine(Point(0,3), Point(4,3))

    @Test
    fun addition_isCorrect() {
        val res = check(pole, position)
        assertEquals(res, result)
    }
    @Test
    fun check(data : List<List<Mark>>, position : Point) {
         val result = data[position.y].fold(1){
                 acc: Int, mark: Mark ->
             if (mark == data[position.y][position.x])
                 acc + 1
                 else
             return@fold acc
         }
        print(result)
        //assertEquals(result, 3)
    }
}