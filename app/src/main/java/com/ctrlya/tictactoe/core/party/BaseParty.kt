package com.ctrlya.tictactoe.core.party

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
    private val _battlefieldStateFlow: MutableStateFlow<List<List<Mark>>> = MutableStateFlow(listOf(listOf<Mark>()))
    val battlefieldStateFlow: StateFlow<List<List<Mark>>> = _battlefieldStateFlow

    private fun isLegalTurn(position: Point): TurnStatus {
        return TurnValidator.validate(position, _marks)
    }

    fun turn(position: Point, mark: Mark) {
        if (isLegalTurn(position) == TurnStatus.SUCCESS) {
            addMark(position, mark)
            _battlefieldStateFlow.value = marks
        }
    }
}