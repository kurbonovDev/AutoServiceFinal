package tj.shonasim.domain.repo

import tj.shonasim.data.entity.Tasks

interface TaskRepository {

    suspend fun insertTask(tasks: Tasks): Long

    suspend fun updateTask(id:Int,desc: String, status: String)

    suspend fun getTask(userId: Int): List<Tasks>

}