package com.soten.todo.presentation.detail

import com.soten.todo.data.entity.TodoEntity

sealed class TodoDetailState {

    object UnInitialized: TodoDetailState()

    object Loading: TodoDetailState()

    data class Success(
        val todoItem: TodoEntity
    ): TodoDetailState()

    object Delete: TodoDetailState()

    object Modify: TodoDetailState()

    object Error: TodoDetailState()

    object Write: TodoDetailState()

}
