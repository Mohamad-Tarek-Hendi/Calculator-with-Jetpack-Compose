package com.example.calculatorwithjetpackcompose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

    var state by mutableStateOf(CalculatorState())
        private set


    fun onEvent(event: CalculatorEvent) {
        when (event) {

            is CalculatorEvent.Number -> enterNumber(event.number)

            is CalculatorEvent.Operation -> enterOperation(event.operation)

            CalculatorEvent.Clear -> state = CalculatorState() // Initialize

            CalculatorEvent.Decimal -> enterDecimal()

            CalculatorEvent.Delete -> performDeletion()

            CalculatorEvent.Calculator -> performCalculator()
        }
    }

    private fun enterNumber(number: Int) {

        if (state.operation == null) {

            if (state.part1.length >= MAX_NUM_LENGTH) {
                return
            }

            state = state.copy(
                part1 = state.part1 + number
            )
            return
        }

        state = state.copy(
            part2 = state.part2 + number
        )

        return

    }

    private fun enterOperation(operation: CalculatorOperation) {
        if (state.part1.isNotBlank()) {
            state = state.copy(operation = operation)
        }
    }

    private fun enterDecimal() {
        if (!state.part1.contains(".") && state.part1.isNotBlank() && state.operation == null) {
            state = state.copy(
                part1 = state.part1 + "."
            )
            return
        }

        if (!state.part2.contains(".") && state.part2.isNotBlank()) {
            state = state.copy(
                part2 = state.part2 + "."
            )
            return
        }

    }

    private fun performDeletion() {
        when {
            state.part1.isNotBlank() -> state = state.copy(
                part1 = state.part1.dropLast(1)
            )

            state.part2.isNotBlank() -> state = state.copy(
                part2 = state.part2.dropLast(1)
            )

            state.operation != null -> state = state.copy(
                operation = null
            )
        }
    }

    private fun performCalculator() {

        val part1 = state.part1.toDoubleOrNull()
        val part2 = state.part2.toDoubleOrNull()

        if (part1 != null && part2 != null) {
            val result = when (state.operation) {

                is CalculatorOperation.Add -> part1 + part2

                is CalculatorOperation.Divide -> part1 / part2

                is CalculatorOperation.Multiply -> part1 * part2

                is CalculatorOperation.Subtract -> part1 - part2

                null -> return
            }

            val formattedResult = if (part1.isInt() && part2.isInt()) {
                result.toInt().toString().take(15)
            } else {
                String.format("%.3f", result)
            }
            state = state.copy(
                part1 = formattedResult,
                part2 = "", operation = null
            )
        }
    }

    private fun Double.isInt(): Boolean {
        return this % 1 == 0.0
    }


    companion object {
        private const val MAX_NUM_LENGTH = 8
    }
}
