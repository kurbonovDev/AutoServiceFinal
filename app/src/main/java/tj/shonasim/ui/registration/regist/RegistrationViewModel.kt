package tj.shonasim.ui.registration.regist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tj.shonasim.data.BaseHandleDBRequest
import tj.shonasim.data.DBResult
import tj.shonasim.data.entity.Users
import tj.shonasim.data.repo.UserRepositoryImpl
import tj.shonasim.domain.repo.UserRepository

class RegistrationViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository = UserRepositoryImpl(application)

    fun insertUser(users: Users): Flow<Triple<String, Boolean, Long?>> = flow {
        val baseHandleDBRequest = BaseHandleDBRequest()

        val request = baseHandleDBRequest.dbRequest {
            userRepository.insertUser(users)
        }
        if (request is DBResult.Success) {
            emit(Triple("Success", true, request.data))
        } else {
            emit(Triple((request as DBResult.Error).errorText, true, null))
        }
    }
}