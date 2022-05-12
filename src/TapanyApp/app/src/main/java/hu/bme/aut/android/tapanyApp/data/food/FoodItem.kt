package hu.bme.aut.android.tapanyApp.data.food

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import hu.bme.aut.android.tapanyApp.data.user.User

@Entity(tableName = "foodItem")
data class FoodItem(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "calorie") var calorie: Double,
    @ColumnInfo(name = "protein") var protein: Double,
    @ColumnInfo(name = "carbs") var carbs: Double,
    @ColumnInfo(name = "fat") var fat: Double,
    @ColumnInfo(name = "amount") var amount: Double,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "other") var other: String,
    @ColumnInfo(name = "userId") var userId: Long?  = null

) {

}
