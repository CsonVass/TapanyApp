package hu.bme.aut.android.tapanyApp.data.user

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE id = :userId")
    fun getUser(userId: Long): User

    @Insert
    fun insert(users: User): Long

    @Update
    fun update(user: User)

    @Delete
    fun deleteItem(user: User)
}
