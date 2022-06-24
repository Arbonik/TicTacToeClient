package com.arbonik.tictactoebot.core.utils

import com.arbonik.tictactoebot.core.Mark


typealias Field = MutableList<MutableList<Mark>>

fun Array<Mark>.convertToString(): String {
    return this.map {
        it.symbol
    }.joinToString(separator = "") {
        it.toString()
    }
}

fun Array<Array<Mark>>.toHash(): String {
    return this.map {
        it.convertToString()
    }.joinToString(separator = "") {
        it
    }
}

val <T> Array<Array<T>>.height
    get() = this.size

val <T> Array<Array<T>>.width
    get() = this.first().size

val <T> List<List<T>>.height
    get() = this.size

val <T> List<List<T>>.width
    get() = this.first().size

fun MutableList<Mark>.convertToString(): String {
    return this.map {
        it.symbol
    }.joinToString(separator = "") {
        it.toString()
    }
}

fun Field.toHash(): String {
    return this.map {
        it.convertToString()
    }.joinToString(separator = "") {
        it
    }
}