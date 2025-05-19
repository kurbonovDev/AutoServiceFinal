package tj.shonasim.data.repo

import android.content.Context
import tj.shonasim.data.entity.Tasks
import tj.shonasim.data.local.AppDatabase
import tj.shonasim.domain.repo.TaskRepository

class TaskRepositoryImpl(context: Context) : TaskRepository {
    private val database = AppDatabase.getDatabase(context)
    private val taskDao = database.getTaskDao()
    override suspend fun insertTask(tasks: Tasks): Long {
        return taskDao.insertTask(tasks)
    }

    override suspend fun updateTask(id: Int, desc: String, status: String) {
        taskDao.updateTaskFields(id, desc = desc, status)
    }


    override suspend fun getTask(userId: Int): List<Tasks> {
        return taskDao.getTasksByUserId(userId)
    }
}