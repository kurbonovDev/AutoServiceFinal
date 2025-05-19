package tj.shonasim.ui.home

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import tj.shonasim.R
import tj.shonasim.data.entity.Car
import tj.shonasim.data.entity.Tasks
import tj.shonasim.databinding.ActivityHomeBinding
import tj.shonasim.ui.home.adapter.EngenerAdapter
import tj.shonasim.ui.home.adapter.UserAdapter
import tj.shonasim.ui.registration.login.LoginActivity
import tj.shonasim.ui.registration.login.LoginActivity.Companion.AUTH
import tj.shonasim.ui.utils.UserPrefsHelper
import tj.shonasim.ui.utils.showCustomSnackBar
import tj.shonasim.ui.utils.showSubmitRequestDialog

class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding: ActivityHomeBinding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(application)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val prefs = getSharedPreferences(AUTH, MODE_PRIVATE)


        val user = UserPrefsHelper.getUser(this)
        var car: Car? = null
        runBlocking {
            viewModel.getCar(user?.id!!).collectLatest {
                if (it.second) {
                    car = it.third
                }
            }
        }
        binding.tvUserName.text = user?.name + " - ${user?.role}"

        if (user?.role == "client") {
            binding.filters.visibility = View.GONE
            lifecycleScope.launch {
                viewModel.getUsto().collectLatest {
                    if (it.second) {
                        if (it.third.isNotEmpty()) {
                            val adapter = UserAdapter(it.third) {
                                showSubmitRequestDialog(this@HomeActivity, onConfirm = {
                                    if (car != null) {
                                        lifecycleScope.launch {
                                            viewModel.insertTask(
                                                Tasks(
                                                    userId = user.id!!,
                                                    carId = car!!.id,
                                                    name = "Машина ${car!!.brand} - ${car!!.year}",
                                                    status = "В процессе",
                                                    engineerId = it.id!!, //engineerId id
                                                    description = ""
                                                )
                                            ).collectLatest {
                                                if (it.second) {
                                                    showCustomSnackBar(
                                                        "Success",
                                                        R.color.green,
                                                        R.color.white
                                                    )
                                                } else {
                                                    showCustomSnackBar(
                                                        "Something go wrong: ${it.first}",
                                                        R.color.green,
                                                        R.color.white
                                                    )
                                                }
                                            }
                                        }
                                    }
                                })
                            }
                            binding.rcView.adapter = adapter
                        } else {
                            binding.emptyView.visibility = View.VISIBLE
                        }
                    } else {
                        Toast.makeText(
                            this@HomeActivity,
                            "Что то пошло не так ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else {
            lifecycleScope.launch {
                viewModel.getTasks(user?.id ?: -1).collectLatest { it ->
                    if (it.second) {
                        if (it.third.isNotEmpty()) {
                            val adapter = EngenerAdapter(this@HomeActivity,it.third) {
                                showEditTaskDialog(task = it)
                            }
                            binding.rcView.adapter = adapter
                        } else {
                            binding.emptyView.visibility = View.VISIBLE
                        }
                    } else {
                        Toast.makeText(
                            this@HomeActivity,
                            "Что то пошло не так ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }



        binding.exit.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                .setTitle("Выход")
                .setMessage("Вы точно хотите выйти?")
                .setPositiveButton("Да") { _, _ ->
                    val sharedPrefs = getSharedPreferences(AUTH, MODE_PRIVATE)
                    sharedPrefs.edit() { clear() }
                    UserPrefsHelper.clearUser(this)

                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Отмена", null)
                .show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
                ContextCompat.getColor(this, R.color.red)
            )
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(
                ContextCompat.getColor(this, R.color.black)
            )
        }

    }


    class HomeViewModelFactory(
        private val application: Application
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private fun showEditTaskDialog(task: Tasks) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)

        val descriptionInput = dialogView.findViewById<EditText>(R.id.etDescription)
        val statusSpinner = dialogView.findViewById<Spinner>(R.id.spinnerStatus)

        // Заполним поля текущими данными задачи
        descriptionInput.setText(task.description)

        val statuses = listOf("В процессе", "Готова")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statuses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statusSpinner.adapter = adapter

        // Устанавливаем текущий статус
        val statusIndex = statuses.indexOf(task.status)
        if (statusIndex != -1) {
            statusSpinner.setSelection(statusIndex)
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Редактировать задачу")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val newDesc = descriptionInput.text.toString().trim()
                val newStatus = statusSpinner.selectedItem.toString()

                if (newDesc.isNotEmpty()) {

                    // Обновим задачу в ViewModel
                    lifecycleScope.launch {
                        viewModel.updateTask(task.id, desc = newDesc,newStatus).collectLatest { 
                            if (it.second){
                                startActivity(Intent(this@HomeActivity,HomeActivity::class.java))
                                finish()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Описание не может быть пустым", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
            ContextCompat.getColor(this, R.color.green) // Убедись, что этот цвет есть
        )
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(
            ContextCompat.getColor(this, R.color.red)
        )
    }


    companion object {
        const val userId = "user_id"
    }
}