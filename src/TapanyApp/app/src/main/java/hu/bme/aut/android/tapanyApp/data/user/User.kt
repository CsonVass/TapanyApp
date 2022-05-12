package hu.bme.aut.android.tapanyApp.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import hu.bme.aut.android.tapanyApp.data.food.FoodItem

@Entity(tableName = "user")
data class User(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "password") var password: String,
    @ColumnInfo(name = "calorie_goal") var calorie_goal: Int
) {

}
