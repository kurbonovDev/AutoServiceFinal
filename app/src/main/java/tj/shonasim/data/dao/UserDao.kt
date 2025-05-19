package tj.shonasim.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import tj.shonasim.data.entity.Users

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: Users): Long

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<Users>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): Users?

    @Query("SELECT * FROM users WHERE login = :login AND password = :password LIMIT 1")
    suspend fun getUserByCredentials(login: String, password: String): Users?

    @Update
    suspend fun updateUser(user: Users)

    @Delete
    suspend fun deleteUser(user: Users)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUserById(userId: Int)

    @Query("SELECT * FROM users WHERE role = :role")
    suspend fun getUsersByRole(role: String = "engineer"): List<Users>
}