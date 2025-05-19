package tj.shonasim.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import tj.shonasim.data.entity.Tasks

@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Tasks): Long

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<Tasks>

    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    suspend fun getTaskById(taskId: Int): Tasks?

    @Query("SELECT * FROM tasks WHERE engineer_id = :userId")
    suspend fun getTasksByUserId(userId: Int): List<Tasks>

    @Query("SELECT * FROM tasks WHERE car_id = :carId")
    suspend fun getTasksByCarId(carId: Int): List<Tasks>

    @Update
    suspend fun updateTask(task: Tasks)

    @Query("UPDATE tasks SET description = :desc, status = :status WHERE id = :id")
    suspend fun updateTaskFields(id: Int, desc: String, status: String)

    @Delete
    suspend fun deleteTask(task: Tasks)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Int)
}