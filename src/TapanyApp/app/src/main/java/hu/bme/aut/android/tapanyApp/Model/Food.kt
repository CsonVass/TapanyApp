package hu.bme.aut.android.tapanyApp.Model

data class Food(
    val foodId: String,
    val label: String,
    val nutrients: Nutrients,
    val category: String,
    val categoryLabel: String,
    val image: String

)
