package hu.bme.aut.android.tapanyApp.Model

data class Measure(
    val uri: String,
    val label: String,
    val qualified: List<Qualifier>?
)
