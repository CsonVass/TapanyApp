package hu.bme.aut.android.tapanyApp.data.food

import androidx.room.*

@Dao
interface FoodItemDao {
    @Query("SELECT * FROM foodItem")
    fun getAll(): List<FoodItem>

    @Query("SELECT DISTINCT date FROM foodItem ORDER BY date")
    fun getDates(): List<String>

    @Query("SELECT DISTINCT date FROM foodItem WHERE userId = :userId ORDER BY date")
    fun getDatesByUserId(userId: Int): List<String>

    @Query("SELECT * FROM foodItem WHERE date = :date")
    fun getItemsByDate(date: String): List<FoodItem>


    @Query("SELECT * FROM foodItem WHERE date = :date AND userId = :userId")
    fun getItemsByDateAndUser(date: String, userId: Int): List<FoodItem>


    @Insert
    fun insert(foodItems: FoodItem): Long

    @Update
    fun update(foodItem: FoodItem)

    @Delete
    fun deleteItem(foodItem: FoodItem)
}
