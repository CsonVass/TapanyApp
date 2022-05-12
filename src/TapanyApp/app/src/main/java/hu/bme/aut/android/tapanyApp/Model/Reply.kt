package hu.bme.aut.android.tapanyApp.Model

import java.util.*

data class Reply (
    val text: String,
    val parsed: List<Parsed>,
    val hints: List<Hint>,
    val _links: Link
)
