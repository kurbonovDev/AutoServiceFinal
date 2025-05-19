package tj.shonasim.ui.registration

import android.app.Application
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.flow
import tj.shonasim.data.BaseHandleDBRequest
import tj.shonasim.data.DBResult
import tj.shonasim.data.entity.Car
import tj.shonasim.data.repo.CarRepositoryImpl
import tj.shonasim.domain.repo.CarRepository

class CarViewModel(application: Application) : ViewModel() {
    private val carRepository: CarRepository = CarRepositoryImpl(application)

    fun insertCar(car: Car) = flow<Triple<String,Boolean,Long>> {
        val baseHandleDBRequest = BaseHandleDBRequest()
        val request = baseHandleDBRequest.dbRequest {
            carRepository.insertCar(car)
        }
        if (request is DBResult.Success) {
            emit(Triple("Success", true, request.data))
        } else {
            emit(Triple((request as DBResult.Error).errorText, false, -1))
        }
    }

}