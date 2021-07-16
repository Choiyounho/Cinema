package com.soten.todo.presentation.list

import com.soten.todo.data.entity.TodoEntity

sealed class TodoListState {

    object UnInitialized: TodoListState()

    object Loading: TodoListState()

    data class Success(
        val todoList: List<TodoEntity>
    ): TodoListState()

    object Error: TodoListState()

}
