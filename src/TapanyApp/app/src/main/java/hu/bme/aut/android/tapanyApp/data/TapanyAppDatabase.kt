package hu.bme.aut.android.tapanyApp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hu.bme.aut.android.tapanyApp.data.food.FoodItem
import hu.bme.aut.android.tapanyApp.data.food.FoodItemDao
import hu.bme.aut.android.tapanyApp.data.user.User
import hu.bme.aut.android.tapanyApp.data.user.UserDao

@Database(entities = [FoodItem::class, User::class], version = 1)
abstract class TapanyAppDatabase : RoomDatabase() {
    abstract fun foodItemDao(): FoodItemDao
    abstract fun userDao(): UserDao

    companion object {
        fun getDatabase(applicationContext: Context): TapanyAppDatabase {
            return Room.databaseBuilder(
                applicationContext,
                TapanyAppDatabase::class.java,
                "tapany-app"
            ).build();
        }
    }
}