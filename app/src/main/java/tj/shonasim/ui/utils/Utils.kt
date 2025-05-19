package tj.shonasim.ui.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import tj.shonasim.R

fun hideKeyboard(activity: Activity) {
    val inputMethodManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = activity.currentFocus ?: View(activity)
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}


fun AppCompatActivity.showCustomSnackBar(
    message: String,
    backgroundColorResId: Int,
    textColorResId: Int
) {
    val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
    snackBar.setBackgroundTint(ContextCompat.getColor(this, backgroundColorResId))
    snackBar.setTextColor(ContextCompat.getColor(this, textColorResId))
    snackBar.show()
}


fun showSubmitRequestDialog(context: Context, onConfirm: () -> Unit) {
    val dialog = AlertDialog.Builder(context)
        .setTitle("Подтверждение")
        .setMessage("Вы хотите подать заявку для данного инженера?")
        .setPositiveButton("Подать заявку") { dialog, _ ->
            onConfirm()
            dialog.dismiss()
        }
        .setNegativeButton("Отмена") { dialog, _ ->
            dialog.dismiss()
        }
        .show()

    dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
        ContextCompat.getColor(context, R.color.green) // Убедись, что этот цвет есть
    )
    dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(
        ContextCompat.getColor(context, R.color.red)
    )
}