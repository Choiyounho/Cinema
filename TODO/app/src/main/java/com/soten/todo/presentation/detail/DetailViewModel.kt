package com.soten.todo.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soten.todo.data.entity.TodoEntity
import com.soten.todo.domain.todo.DeleteTodoItemUseCase
import com.soten.todo.domain.todo.GetTodoItemUseCase
import com.soten.todo.domain.todo.InsertTodoItemUseCase
import com.soten.todo.domain.todo.UpdateTodoUseCase
import com.soten.todo.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class DetailViewModel(
    var detailMode: DetailMode,
    var id: Long = -1,
    private val getTodoItemUseCase: GetTodoItemUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
    private val insertTodoItemUseCase: InsertTodoItemUseCase
) : BaseViewModel() {

    private var _todoDetailLiveData =
        MutableLiveData<TodoDetailState>(TodoDetailState.UnInitialized)
    val todoDetailLiveData: LiveData<TodoDetailState> = _todoDetailLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        when (detailMode) {
            DetailMode.WRITE -> {
                _todoDetailLiveData.postValue(TodoDetailState.Write)
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
            deleteTodoItemUseCase(id)
            _todoDetailLiveData.postValue(TodoDetailState.Delete)

        } catch (e: Exception) {
            e.printStackTrace()
            _todoDetailLiveData.postValue(TodoDetailState.Error)
        }
    }

    fun writeTodo(title: String, description: String) = viewModelScope.launch {
        _todoDetailLiveData.postValue(TodoDetailState.Loading)
        when (detailMode) {
            DetailMode.WRITE -> {
                try {
                    val todoEntity = TodoEntity(
                        title = title,
                        description = description
                    )
                    id = insertTodoItemUseCase(todoEntity)
                    _todoDetailLiveData.postValue(TodoDetailState.Success(todoEntity))
                    detailMode = DetailMode.DETAIL
                } catch (e: Exception) {
                    e.printStackTrace()
                    _todoDetailLiveData.postValue(TodoDetailState.Error)
                }
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