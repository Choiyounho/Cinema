package com.soten.todo.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import com.soten.todo.data.entity.TodoEntity
import com.soten.todo.domain.todo.GetTodoListUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * 필요한 UseCase
 * 1. [GetTodoListUseCase]
 * 2. [UpdateTodoUseCase]
 * 3. [DeleteAllTodoItemUseCase]
 */
internal class ListViewModel(
    private val getTodoListUseCase: GetTodoListUseCase
): ViewModel() {

    private var _todoListLiveData = MutableLiveData<List<TodoEntity>>()
    val todoListLiveData: LiveData<List<TodoEntity>> = _todoListLiveData

    fun fetchData(): Job = viewModelScope.launch {
        _todoListLiveData.postValue(getTodoListUseCase())
    }
}