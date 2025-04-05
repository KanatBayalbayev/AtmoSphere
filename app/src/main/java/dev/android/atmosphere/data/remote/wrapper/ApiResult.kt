package dev.android.atmosphere.data.remote.wrapper

import dev.android.atmosphere.data.util.ErrorMessages.HTTP_ERROR
import dev.android.atmosphere.data.util.ErrorMessages.formatNetworkError
import dev.android.atmosphere.data.util.ErrorMessages.formatUnknownError
import dev.android.atmosphere.domain.model.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException


sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(
        val message: String,
        val code: Int? = null,
        val exception: Exception? = null
    ) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()

    fun toResource(): DataState<@UnsafeVariance T> {
        return when (this) {
            is Success -> DataState.Success(data)
            is Error -> DataState.Error(message)
            is Loading -> DataState.Loading
        }
    }

    fun <R> map(transform: (T) -> R): ApiResult<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> Error(message, code, exception)
            is Loading -> Loading
        }
    }

    fun getOrNull(): T? {
        return when (this) {
            is Success -> data
            else -> null
        }
    }

    companion object {
        suspend fun <T> safeApiCall(
            apiCall: suspend () -> T
        ): ApiResult<T> = withContext(Dispatchers.IO) {
            try {
                Success(apiCall())
            } catch (e: IOException) {
                Error(formatNetworkError(e.message), exception = e)
            } catch (e: retrofit2.HttpException) {
                Error(HTTP_ERROR, e.code(), e)
            } catch (e: Exception) {
                Error(formatUnknownError(e.message), exception = e)
            }
        }
    }
}