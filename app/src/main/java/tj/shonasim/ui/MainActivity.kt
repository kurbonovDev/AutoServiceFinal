package tj.shonasim.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tj.shonasim.R
import tj.shonasim.ui.home.HomeActivity
import tj.shonasim.ui.registration.login.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences(LoginActivity.AUTH, MODE_PRIVATE)
        val isLoggedIn = prefs.getBoolean(LoginActivity.IS_LOGGED, false)

        if (isLoggedIn) {
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()

        setContentView(R.layout.activity_main)
    }
}