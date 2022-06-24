package com.ctrlya.tictactoe.canvas

enum class Mark(
    val symbol: Char
) {
    EMPTY('_'),
    X('x'),
    O('o');

    override fun toString(): String {
        return symbol.toString()
    }
}