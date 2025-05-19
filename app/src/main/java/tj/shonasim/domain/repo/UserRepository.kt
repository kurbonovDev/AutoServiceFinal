package tj.shonasim.domain.repo

import tj.shonasim.data.entity.Users

interface UserRepository {

    suspend fun insertUser(user: Users):Long


    suspend fun loginUser(login: String, password: String): Users?

    suspend fun getUser(id: Int): Users?

    suspend fun getUsto(): List<Users>


}