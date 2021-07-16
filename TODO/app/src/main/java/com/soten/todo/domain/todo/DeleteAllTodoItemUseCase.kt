package com.soten.todo.domain.todo

import com.soten.todo.data.repository.TodoRepository

internal class DeleteAllTodoItemUseCase(
    private val todoRepository: TodoRepository
) {

    suspend operator fun invoke() {
        return todoRepository.deleteAll()
    }

}