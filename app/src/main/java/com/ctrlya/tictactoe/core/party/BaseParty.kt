package com.ctrlya.tictactoe.core.party

import android.util.Log
import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.data.Point
import com.ctrlya.tictactoe.core.domain.Battlefield
import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// отвечает за валидацию хода, предоставляет stateFlow поля
open class BaseParty(settings: BattlefieldSettings) : Battlefield(
    settings
) {
    private val _battlefieldStateFlow: MutableStateFlow<List<List<Mark>>> = MutableStateFlow(listOf(listOf<Mark>())).apply { value = marks }
    val battlefieldStateFlow: StateFlow<List<List<Mark>>> = _battlefieldStateFlow

    private fun isLegalTurn(position: Point): TurnStatus {
        var pos = Point(position.y, position.x)
        return TurnValidator.validate(position, _marks)
    }

    fun turn(position: Point, mark: Mark) : TurnStatus {
        val isLegal = isLegalTurn(position)
        if (isLegal == TurnStatus.SUCCESS) {
            addMark(position, mark)
            _battlefieldStateFlow.value = marks
            TurnStatus.SUCCESS
        }
        return isLegal
    }
}