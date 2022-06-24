package com.arbonik.tictactoebot.core

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