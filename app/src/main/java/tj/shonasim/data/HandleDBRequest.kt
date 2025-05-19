package tj.shonasim.data

interface HandleDBRequest {
    suspend fun <T> dbRequest(request: suspend () -> T): DBResult<T>
}