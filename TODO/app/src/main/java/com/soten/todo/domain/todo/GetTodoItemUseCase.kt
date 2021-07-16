package com.soten.todo.domain.todo

import com.soten.todo.data.entity.TodoEntity
import com.soten.todo.data.repository.TodoRepository

internal class GetTodoItemUseCase(
    private val todoRepository: TodoRepository
) {

    suspend operator fun invoke(itemId: Long): TodoEntity? {
        return todoRepository.getTodoItem(itemId)
    }
}