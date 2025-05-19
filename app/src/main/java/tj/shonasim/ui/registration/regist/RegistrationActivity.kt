package tj.shonasim.ui.registration.regist

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tj.shonasim.R
import tj.shonasim.data.entity.Users
import tj.shonasim.databinding.ActivityRegistrationBinding
import tj.shonasim.ui.registration.CarActivity
import tj.shonasim.ui.utils.hideKeyboard
import tj.shonasim.ui.utils.showCustomSnackBar

class RegistrationActivity : AppCompatActivity() {

    private var _binding: ActivityRegistrationBinding? = null
    private val binding: ActivityRegistrationBinding get() = _binding!!

    private val viewModel: RegistrationViewModel by viewModels {
        RegistrationViewModelFactory(application)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreateAccount.setOnClickListener {
            val fullName = binding.edName.text.toString().trim()
            val login = binding.edLogin.text.toString().trim()
            val password = binding.edPassword.text.toString().trim()
            val phoneNumber = binding.edPhone.text.toString().trim()

            var hasError = false

            if (fullName.isEmpty()) {
                binding.secInputContainer1.error = "Введите имя"
                hasError = true
            } else {
                binding.secInputContainer1.error = null
            }

            if (login.isEmpty()) {
                binding.secInputContainer3.error = "Введите логин"
                hasError = true
            } else {
                binding.secInputContainer3.error = null
            }

            if (password.length < 6) {
                binding.secInputContainer4.error = "Пароль должен быть от 6 символов"
                hasError = true
            } else {
                binding.secInputContainer4.error = null
            }

            if (phoneNumber.isEmpty()) {
                binding.secInputContainer2.error = "Введите телефон"
                hasError = true
            } else {
                binding.secInputContainer2.error = null
            }

            if (hasError) return@setOnClickListener

            hideKeyboard(this)
            val newUser = Users(
                id = null,
                name = fullName,
                login = login,
                password = password,
                phone = phoneNumber,
                role = "client"
            )

            // Роли
            val roles = arrayOf("engineer", "client")
            var selectedRole = roles[1]

            val roleDialog = AlertDialog.Builder(this)
                .setTitle("Выберите роль")
                .setSingleChoiceItems(roles, 1) { _, which ->
                    selectedRole = roles[which]
                }
                .setPositiveButton("Продолжить") { _, _ ->
                    if (selectedRole == "engineer") {
                        val input = EditText(this).apply {
                            hint = "Введите секретный пароль"
                            inputType =
                                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                            setPadding(50, 30, 50, 30)
                        }

                        val dialog = AlertDialog.Builder(this)
                            .setTitle("Подтвердите доступ")
                            .setView(input)
                            .setPositiveButton("ОК") { _, _ ->
                                val enteredPassword = input.text.toString()
                                val correctPassword = "123456" // <- здесь ваш статичный пароль

                                if (enteredPassword == correctPassword) {
                                    showCustomSnackBar(
                                        "Успещно",
                                        R.color.green,
                                        R.color.white
                                    )
                                    registerUser(
                                        fullName,
                                        login,
                                        password,
                                        phoneNumber,
                                        selectedRole
                                    )
                                } else {
                                    showCustomSnackBar(
                                        "Неверный пароль для создание engineer",
                                        R.color.red,
                                        R.color.white
                                    )
                                }
                            }
                            .setNegativeButton("Отмена", null)
                            .show()

                        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.green
                            )
                        )
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(
                            ContextCompat.getColor(this, R.color.black)
                        )
                    } else {
                        registerUser(fullName, login, password, phoneNumber, selectedRole, true)
                    }

                }
                .setNegativeButton("Отмена", null)
                .create()

            roleDialog.show()

            roleDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
                ContextCompat.getColor(this, R.color.red) // Убедись, что этот цвет есть
            )
            roleDialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(
                ContextCompat.getColor(this, R.color.black)
            )


        }
    }

    class RegistrationViewModelFactory(
        private val application: Application
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RegistrationViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private fun registerUser(
        fullName: String,
        login: String,
        password: String,
        phoneNumber: String,
        role: String,
        needAddCar: Boolean = false
    ) {
        hideKeyboard(this)
        val newUser = Users(
            id = null,
            name = fullName,
            login = login,
            password = password,
            phone = phoneNumber,
            role = role
        )

        lifecycleScope.launch {
            viewModel.insertUser(newUser).collectLatest {
                if (it.second) {
                    showCustomSnackBar(it.first, R.color.green, R.color.white)
                    if (needAddCar) {
                        startActivity(
                            Intent(
                                this@RegistrationActivity,
                                CarActivity::class.java
                            ).apply {
                                putExtra("user_id", it.third)
                            })
                    }
                    finish()
                } else {
                    showCustomSnackBar(it.first, R.color.red, R.color.white)
                }
            }
        }
    }
}