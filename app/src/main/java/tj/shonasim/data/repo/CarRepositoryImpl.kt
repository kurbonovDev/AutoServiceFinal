package tj.shonasim.data.repo

import android.app.Application
import tj.shonasim.data.entity.Car
import tj.shonasim.data.local.AppDatabase
import tj.shonasim.domain.repo.CarRepository

class CarRepositoryImpl(application: Application) : CarRepository {

    private val database = AppDatabase.getDatabase(application)
    private val carDao = database.getCarDao()

    override suspend fun insertCar(car: Car):Long {
        return carDao.insertCar(car)
    }

    override suspend fun getCar(userId: Int): Car? {
        return carDao.getCarById(userId)
    }
}