package tj.shonasim.ui.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import tj.shonasim.data.entity.Users

object UserPrefsHelper {

    private const val PREF_NAME = "user_prefs"
    private const val USER_KEY = "user_data"
    private const val CAR_DATA = "car_data"

    fun saveUser(context: Context, user: Users) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = Gson().toJson(user)
        prefs.edit().putString(USER_KEY, json).apply()
    }

    fun getUser(context: Context): Users? {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(USER_KEY, null)
        return if (json != null) Gson().fromJson(json, Users::class.java) else null
    }

    fun clearUser(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(USER_KEY).apply()
    }
}