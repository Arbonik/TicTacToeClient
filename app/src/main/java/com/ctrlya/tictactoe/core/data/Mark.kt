package com.ctrlya.tictactoe.core.data

enum class Mark(
    val code: Int
) {
    EMPTY(0),
    X(1),
    O(2);
    override fun toString(): String {
        return code.toString()
    }
}
