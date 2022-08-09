package com.gabo.flow

import androidx.lifecycle.ViewModel
import com.gabo.flow.model.Content
import com.gabo.flow.service.RetrofitInstance.getService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainVM : ViewModel() {
    fun getList(): Flow<Result<Content.ItemModel>> = flow {
        val answerFromServer = getService().getItems()
        val response: Result<Content.ItemModel> = when {
            answerFromServer.isSuccessful -> {
                Result.Success(list = answerFromServer.body()!!.content)
            }
            else -> {
                Result.Error(errorMSg = answerFromServer.errorBody().toString())
            }
        }
        emit(response)
    }
}

sealed class Result<T> {
    data class Success<T>(val list: List<T>) : Result<T>()
    data class Error<T>(val errorMSg: String) : Result<T>()
}