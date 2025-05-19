package tj.shonasim.data.repo

import android.content.Context
import tj.shonasim.data.entity.Users
import tj.shonasim.data.local.AppDatabase
import tj.shonasim.domain.repo.UserRepository

class UserRepositoryImpl(context: Context) : UserRepository {
    private val database = AppDatabase.getDatabase(context)
    private val userDao = database.getUserDao()

    override suspend fun insertUser(user: Users):Long {
        return userDao.insertUser(user)
    }

    override suspend fun loginUser(login: String, password: String): Users? {
        return userDao.getUserByCredentials(login, password)
    }

    override suspend fun getUser(id: Int): Users? {
        return userDao.getUserById(id)
    }

    override suspend fun getUsto(): List<Users> {
        return userDao.getUsersByRole()
    }

}