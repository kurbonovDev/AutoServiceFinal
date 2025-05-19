package tj.shonasim.ui.home

import android.app.Application
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tj.shonasim.data.BaseHandleDBRequest
import tj.shonasim.data.DBResult
import tj.shonasim.data.entity.Car
import tj.shonasim.data.entity.Tasks
import tj.shonasim.data.entity.Users
import tj.shonasim.data.repo.CarRepositoryImpl
import tj.shonasim.data.repo.TaskRepositoryImpl
import tj.shonasim.data.repo.UserRepositoryImpl
import tj.shonasim.domain.repo.CarRepository
import tj.shonasim.domain.repo.TaskRepository
import tj.shonasim.domain.repo.UserRepository

class HomeViewModel(application: Application) : ViewModel() {
    private val userRepository: UserRepository = UserRepositoryImpl(application)
    private val taskRepository: TaskRepository = TaskRepositoryImpl(application)
    private val carRepository: CarRepository = CarRepositoryImpl(application)

    fun getUser(id: Int): Flow<Triple<String, Boolean, Users?>> = flow {
        val baseHandleDBRequest = BaseHandleDBRequest()

        val request = baseHandleDBRequest.dbRequest {
            userRepository.getUser(id)
        }
        if (request is DBResult.Success) {
            emit(Triple("Success", true, request.data))
        } else {
            emit(Triple((request as DBResult.Error).errorText, false, null))
        }
    }


    fun getUsto(): Flow<Triple<String, Boolean, List<Users>>> = flow {
        val baseHandleDBRequest = BaseHandleDBRequest()

        val request = baseHandleDBRequest.dbRequest {
            userRepository.getUsto()
        }
        if (request is DBResult.Success) {
            emit(Triple("Success", true, request.data))
        } else {
            emit(Triple((request as DBResult.Error).errorText, false, emptyList()))
        }
    }

    fun insertTask(tasks: Tasks): Flow<Triple<String, Boolean, Long>> = flow {
        val baseHandleDBRequest = BaseHandleDBRequest()

        val request = baseHandleDBRequest.dbRequest {
            taskRepository.insertTask(tasks)
        }
        if (request is DBResult.Success) {
            emit(Triple("Success", true, request.data))
        } else {
            emit(Triple((request as DBResult.Error).errorText, false, -1))
        }
    }


    fun getCar(userId: Int): Flow<Triple<String, Boolean, Car?>> = flow {
        val baseHandleDBRequest = BaseHandleDBRequest()

        val request = baseHandleDBRequest.dbRequest {
            carRepository.getCar(userId)
        }
        if (request is DBResult.Success) {
            emit(Triple("Success", true, request.data))
        } else {
            emit(Triple((request as DBResult.Error).errorText, false, null))
        }
    }

    fun getTasks(userId: Int): Flow<Triple<String, Boolean, List<Tasks>>> = flow {
        val baseHandleDBRequest = BaseHandleDBRequest()

        val request = baseHandleDBRequest.dbRequest {
            taskRepository.getTask(userId)
        }
        if (request is DBResult.Success) {
            emit(Triple("Success", true, request.data))
        } else {
            emit(Triple((request as DBResult.Error).errorText, false, emptyList()))
        }
    }


    fun updateTask(id: Int, desc: String, status: String): Flow<Pair<String, Boolean>> = flow {
        val baseHandleDBRequest = BaseHandleDBRequest()

        val request = baseHandleDBRequest.dbRequest {
            taskRepository.updateTask(id, desc, status)
        }
        if (request is DBResult.Success) {
            emit(Pair("Success", true))
        } else {
            emit(Pair((request as DBResult.Error).errorText, false))
        }
    }
}