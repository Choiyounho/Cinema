package com.soten.todo.domain.todo

import com.soten.todo.data.repository.TodoRepository
import com.soten.todo.domain.UseCase

internal class DeleteTodoItemUseCase(
    private val todoRepository: TodoRepository
): UseCase {

    suspend operator fun invoke(itemId: Long) {
        return todoRepository.delete(itemId)
    }
}