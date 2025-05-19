package tj.shonasim.ui.registration

import android.app.Application
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tj.shonasim.R
import tj.shonasim.data.entity.Car
import tj.shonasim.databinding.ActivityCarBinding
import tj.shonasim.ui.utils.UserPrefsHelper
import tj.shonasim.ui.utils.showCustomSnackBar

class CarActivity : AppCompatActivity() {

    private var _binding: ActivityCarBinding? = null
    private val binding: ActivityCarBinding get() = _binding!!

    private val viewModel: CarViewModel by viewModels {
        CarViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddCar.setOnClickListener {
            val brand = binding.edBrand.text.toString().trim()
            val model = binding.edModel.text.toString().trim()
            val yearText = binding.edYear.text.toString().trim()

            var hasError = false

            if (brand.isEmpty()) {
                binding.edBrand.error = "Введите марку"
                hasError = true
            }

            if (model.isEmpty()) {
                binding.edModel.error = "Введите модель"
                hasError = true
            }

            if (yearText.length != 4) {
                binding.edYear.error = "Введите корректный год (4 цифры)"
                hasError = true
            }

            if (hasError) return@setOnClickListener

            val year = yearText.toInt()


            val userId = intent.getLongExtra("user_id", -1).toInt()

            if (userId == -1) {
                showCustomSnackBar("Ошибка: пользователь не найден", R.color.red, R.color.white)
                return@setOnClickListener
            }

            val car = Car(
                userId = userId,
                brand = brand,
                model = model,
                year = year
            )

            lifecycleScope.launch {
                viewModel.insertCar(car).collectLatest {
                    if (it.second) {
                        val newCar = Car(
                            id = it.third.toInt(),
                            userId = userId,
                            brand,
                            model,
                            year
                        )
                        showCustomSnackBar(it.first, R.color.green, R.color.white)
                        finish()
                    } else {
                        showCustomSnackBar(it.first, R.color.red, R.color.white)
                    }
                }
            }
        }
    }


    class CarViewModelFactory(
        private val application: Application
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CarViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CarViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}