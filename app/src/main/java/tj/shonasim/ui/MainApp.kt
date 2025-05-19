package tj.shonasim.ui

import android.app.Application
import tj.shonasim.data.local.AppDatabase

class MainApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
}