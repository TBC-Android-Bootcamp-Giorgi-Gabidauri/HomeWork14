package com.gabo.flow

import androidx.lifecycle.ViewModel
import com.gabo.flow.model.Content
import com.gabo.flow.model.YesNoModel
import com.gabo.flow.service.RetrofitInstance.getService
import com.gabo.flow.service.RetrofitInstance.getYesnoService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class MainVM : ViewModel() {

    val state: MutableStateFlow<Result.Success<List<Content.ItemModel>>> =
        MutableStateFlow(Result.Success(emptyList()))


    fun getYesOrNo() = flow {
        val answerFromServer = getYesnoService().getYesNoModel()
        val response: Result<YesNoModel> = when {
            answerFromServer.isSuccessful -> {
                Result.Success(list = answerFromServer.body()!!)
            }
            else -> {
                Result.Error(errorMSg = answerFromServer.errorBody().toString())
            }
        }
        emit(response)
    }

    fun getList(): Flow<Result<Content>> = flow {
        val answerFromServer = getService().getItems()
        var stateResponse: Result.Success<Content>? = null
        val response: Result<Content> = when {
            answerFromServer.isSuccessful -> {
                stateResponse = Result.Success(answerFromServer.body()!!)
                Result.Success(list = answerFromServer.body()!!)
            }
            else -> {
                Result.Error(errorMSg = answerFromServer.errorBody().toString())
            }
        }
        emit(response)
        stateResponse?.let {
            state.value = Result.Success(it.list.content)
        }
    }
}

sealed class Result<T> {
    data class Success<T>(val list: T) : Result<T>()
    data class Error<T>(val errorMSg: String) : Result<T>()
}