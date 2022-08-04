package com.gabo.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabo.flow.model.Content
import com.gabo.flow.service.RetrofitInstance.getService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainVM : ViewModel() {
    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState())
    val viewState = _viewState

    private fun currentViewState() = ViewState()

    init {
        getList()
    }

    fun getList() {
        viewModelScope.launch {
            _viewState.value = (currentViewState().copy(isLoading = true))
            val result = getService().getItems()
            if (result.isSuccessful) {
                _viewState.value = (
                        currentViewState().copy(
                            isSuccessful = true,
                            isLoading = false,
                            list = result.body()!!
                        ))
            } else {
                _viewState.value = (
                        currentViewState().copy(
                            isSuccessful = false,
                            isLoading = false,
                            errorMsg = result.errorBody().toString()
                        ))
            }
        }
    }

    data class ViewState(
        val isSuccessful: Boolean? = null,
        val errorMsg: String? = null,
        val list: Content? = null,
        val isLoading: Boolean = false
    )
}