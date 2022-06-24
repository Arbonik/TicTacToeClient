package com.arbonik.tictactoebot.core

import com.arbonik.tictactoebot.core.Mark
import main.utils.transformers.FieldTransformer

class FieldMather {
    companion object {
        fun assert(
            first: MutableList<MutableList<Mark>>,
            second: MutableList<MutableList<Mark>>,
        ): Boolean {
            return first == second ||
                    first == FieldTransformer.rotate(second) ||
                    first == FieldTransformer.rotate(second) ||
                    first == FieldTransformer.rotate(second)
        }
    }
}
