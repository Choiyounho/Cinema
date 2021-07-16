package com.soten.todo.domain.todo

import com.soten.todo.data.repository.TodoRepository
import com.soten.todo.domain.UseCase

internal class DeleteAllTodoItemUseCase(
    private val todoRepository: TodoRepository
): UseCase {

    suspend operator fun invoke() {
        return todoRepository.deleteAll()
    }

}