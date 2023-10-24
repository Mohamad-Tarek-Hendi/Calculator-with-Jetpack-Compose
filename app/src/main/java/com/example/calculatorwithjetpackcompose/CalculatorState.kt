package com.example.calculatorwithjetpackcompose

data class CalculatorState(
    val part1: String = "",
    val operation: CalculatorOperation? = null,
    val part2: String = ""
)
