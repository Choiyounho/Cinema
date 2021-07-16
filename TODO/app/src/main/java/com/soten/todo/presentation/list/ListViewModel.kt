package com.soten.todo.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import com.soten.todo.data.entity.TodoEntity
import com.soten.todo.domain.todo.DeleteAllTodoItemUseCase
import com.soten.todo.domain.todo.GetTodoListUseCase
import com.soten.todo.domain.todo.UpdateTodoUseCase
import com.soten.todo.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * 필요한 UseCase
 * 1. [GetTodoListUseCase]
 * 2. [UpdateTodoUseCase]
 * 3. [DeleteAllTodoItemUseCase]
 */
internal class ListViewModel(
    private val getTodoListUseCase: GetTodoListUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
    private val deleteAllTodoItemUseCase: DeleteAllTodoItemUseCase
): BaseViewModel() {

    private var _todoListLiveData = MutableLiveData<TodoListState>(TodoListState.UnInitialized)
    val todoListLiveData: LiveData<TodoListState> = _todoListLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        _todoListLiveData.postValue(TodoListState.Loading)
        _todoListLiveData.postValue(TodoListState.Success(getTodoListUseCase()))
    }

    fun updateEntity(todo: TodoEntity) = viewModelScope.launch {
        updateTodoUseCase(todo)
    }

    fun deleteAll() = viewModelScope.launch {
        _todoListLiveData.postValue(TodoListState.Loading)
        deleteAllTodoItemUseCase()
        _todoListLiveData.postValue(TodoListState.Success(getTodoListUseCase()))
    }
}