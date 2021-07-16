package com.soten.todo.domain.todo

import com.soten.todo.data.repository.TodoRepository

internal class DeleteTodoItemUseCase(
    private val todoRepository: TodoRepository
) {

    suspend operator fun invoke(itemId: Long) {
        return todoRepository.deleteTodoItem(itemId)
    }
}