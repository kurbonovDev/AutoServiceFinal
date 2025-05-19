package tj.shonasim.data

import android.database.sqlite.SQLiteException

class BaseHandleDBRequest : HandleDBRequest {
    override suspend fun <T> dbRequest(request: suspend () -> T): DBResult<T> {
        return try {
            val result = request.invoke()
            if (result != null) {
                DBResult.Success(result)
            } else {
                DBResult.Error("Данные не найдены (null)")
            }
        } catch (e: Exception) {
            val errorText = when (e) {
                is SQLiteException -> "Ошибка SQL: $e"
                is NullPointerException -> "Ошибка NullPointerException: $e"
                else -> "Ошибка: $e"
            }
            DBResult.Error(errorText)
        }
    }
}