package com.soten.todo.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soten.todo.domain.todo.DeleteTodoItemUseCase
import com.soten.todo.domain.todo.GetTodoItemUseCase
import com.soten.todo.domain.todo.UpdateTodoUseCase
import com.soten.todo.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class DetailViewModel(
    var detailMode: DetailMode,
    var id: Long = -1,
    private val getTodoItemUseCase: GetTodoItemUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase
): BaseViewModel() {

    private var _todoDetailLiveData = MutableLiveData<TodoDetailState>(TodoDetailState.UnInitialized)
    val todoDetailLiveData: LiveData<TodoDetailState> = _todoDetailLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        when (detailMode) {
            DetailMode.WRITE -> {
                // 나중에 작성모드로 상세화면 진입 로직 처리
            }

            DetailMode.DETAIL -> {
                _todoDetailLiveData.postValue(TodoDetailState.Loading)
                try {
                    getTodoItemUseCase(id)?.let {
                        _todoDetailLiveData.postValue(TodoDetailState.Success(it))
                    } ?: run {
                        _todoDetailLiveData.postValue(TodoDetailState.Error)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _todoDetailLiveData.postValue(TodoDetailState.Error)
                }
            }
        }
    }

    fun deleteTodo() = viewModelScope.launch {
        _todoDetailLiveData.postValue(TodoDetailState.Loading)

        try {
            if (deleteTodoItemUseCase(id)) {
                _todoDetailLiveData.postValue(TodoDetailState.Delete)
            } else {
                _todoDetailLiveData.postValue(TodoDetailState.Error)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _todoDetailLiveData.postValue(TodoDetailState.Error)
        }
    }

    fun writeTodo(title: String, description: String) = viewModelScope.launch {
        _todoDetailLiveData.postValue(TodoDetailState.Loading)
        when (detailMode) {
            DetailMode.WRITE -> {

            }

            DetailMode.DETAIL -> {
                try {
                    getTodoItemUseCase(id)?.let {
                        val updateTodoEntity = it.copy(title = title, description = description)

                        updateTodoUseCase(updateTodoEntity)
                        _todoDetailLiveData.postValue(TodoDetailState.Success(updateTodoEntity))
                    } ?: run {
                        _todoDetailLiveData.postValue(TodoDetailState.Error)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _todoDetailLiveData.postValue(TodoDetailState.Error)
                }
            }
        }
    }

}