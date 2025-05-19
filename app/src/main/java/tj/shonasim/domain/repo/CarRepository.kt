package tj.shonasim.domain.repo

import tj.shonasim.data.entity.Car

interface CarRepository {
    suspend fun insertCar(car: Car): Long
    suspend fun getCar(userId: Int): Car?
}