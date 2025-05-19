package tj.shonasim.data

sealed class DBResult<out T> {
    data class Success<out T>(val data: T) : DBResult<T>()
    data class Error(val errorText: String) : DBResult<Nothing>()
}