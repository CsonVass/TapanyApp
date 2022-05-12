package hu.bme.aut.android.tapanyApp.Model

import java.util.*

data class Parsed(
    val food: Food,
    val quantity: Double,
    val measure: Measure?
)
