package tj.shonasim.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import tj.shonasim.data.entity.Car

@Dao
interface CarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(car: Car): Long

    @Query("SELECT * FROM car")
    suspend fun getAllCars(): List<Car>

    @Query("SELECT * FROM car WHERE id = :id LIMIT 1")
    suspend fun getCarById(id: Int): Car?

    @Query("SELECT * FROM car WHERE user_id = :userId")
    suspend fun getCarsByUserId(userId: Int): List<Car>

    @Update
    suspend fun updateCar(car: Car)

    @Delete
    suspend fun deleteCar(car: Car)

    @Query("DELETE FROM car WHERE id = :carId")
    suspend fun deleteCarById(carId: Int)
}