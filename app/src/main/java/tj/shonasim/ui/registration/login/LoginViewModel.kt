package tj.shonasim.ui.registration.login

import android.app.Application
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tj.shonasim.data.BaseHandleDBRequest
import tj.shonasim.data.DBResult
import tj.shonasim.data.entity.Users
import tj.shonasim.data.repo.UserRepositoryImpl
import tj.shonasim.domain.repo.UserRepository

class LoginViewModel(application: Application) : ViewModel() {
    private val userRepository: UserRepository = UserRepositoryImpl(application)

    fun loginUser(login: String, password: String): Flow<Triple<String, Boolean, Users?>> = flow {
        val baseHandleDBRequest = BaseHandleDBRequest()

        val request = baseHandleDBRequest.dbRequest {
            userRepository.loginUser(login, password)
        }
        if (request is DBResult.Success) {
            emit(Triple("Success", true, request.data))
        } else {
            emit(Triple((request as DBResult.Error).errorText, false, null))
        }
    }
}