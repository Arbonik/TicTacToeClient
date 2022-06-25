package com.ctrlya.tictactoe.core.party.transformers

import com.ctrlya.tictactoe.core.data.Mark


class FieldTransformer {
    companion object {
        fun rotate(
            a: MutableList<MutableList<Mark>>
        ): MutableList<MutableList<Mark>> {
            for (i in 0 until a.size / 2) {
                for (j in i until a.size - i - 1) {
                    // Swap elements of each cycle
                    // in clockwise direction
                    val temp = a[i][j]
                    a[i][j] = a[a.size - 1 - j][i]
                    a[a.size - 1 - j][i] = a[a.size - 1 - i][a.size - 1 - j]
                    a[a.size - 1 - i][a.size - 1 - j] = a[j][a.size - 1 - i]
                    a[j][a.size - 1 - i] = temp
                }
            }
            return a
        }

        fun <T> rotate(
            a: Array<Array<T>>
        ): Array<Array<T>> {
            val b = a.copyOf()
            for (i in 0 until a.size / 2) {
                for (j in i until a.size - i - 1) {
                    val temp = b[i][j]
                    b[i][j] = b[b.size - 1 - j][i]
                    b[b.size - 1 - j][i] = b[b.size - 1 - i][b.size - 1 - j]
                    b[b.size - 1 - i][b.size - 1 - j] = b[j][b.size - 1 - i]
                    b[j][b.size - 1 - i] = temp
                }
            }
            return b
        }
    }
}