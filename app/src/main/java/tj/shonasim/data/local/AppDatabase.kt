package tj.shonasim.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tj.shonasim.data.dao.CarDao
import tj.shonasim.data.dao.TasksDao
import tj.shonasim.data.dao.UserDao
import tj.shonasim.data.entity.Car
import tj.shonasim.data.entity.Tasks
import tj.shonasim.data.entity.Users

@Database(
    entities = [Car::class, Tasks::class, Users::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {


    abstract fun getUserDao(): UserDao
    abstract fun getTaskDao(): TasksDao
    abstract fun getCarDao(): CarDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "auto_service"

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java,
                    DB_NAME
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }

        }
    }
}