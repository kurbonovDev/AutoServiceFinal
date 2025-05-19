package tj.shonasim.ui.registration.login

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tj.shonasim.R
import tj.shonasim.data.entity.Users
import tj.shonasim.databinding.ActivityLoginBinding
import tj.shonasim.ui.home.HomeActivity
import tj.shonasim.ui.registration.regist.RegistrationActivity
import tj.shonasim.ui.utils.UserPrefsHelper
import tj.shonasim.ui.utils.hideKeyboard
import tj.shonasim.ui.utils.showCustomSnackBar

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding: ActivityLoginBinding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.tvRegist.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val login = binding.edLogin.text.toString().trim()
            val password = binding.edPassword.text.toString().trim()

            hideKeyboard(this)
            lifecycleScope.launch {
                viewModel.loginUser(login, password).collectLatest {
                    if (it.second) {
                        val prefs =
                            this@LoginActivity.getSharedPreferences(AUTH, MODE_PRIVATE)

                        prefs.edit() { putBoolean(IS_LOGGED, true) }
                        UserPrefsHelper.saveUser(this@LoginActivity, user = it.third ?: Users())

                        showCustomSnackBar(it.first, R.color.green, R.color.white)
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java).apply {
                            putExtra(HomeActivity.userId, it.third?.id ?: -1)
                        })
                        finish()
                    } else {
                        showCustomSnackBar(it.first, R.color.red, R.color.white)
                    }
                }

            }
        }

    }

    class LoginViewModelFactory(
        private val application: Application
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object {
        const val AUTH = "auth_prefs"
        const val IS_LOGGED = "is_logged_in"
    }
}