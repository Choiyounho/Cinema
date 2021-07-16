package com.soten.todo.domain.todo

import com.soten.todo.data.entity.TodoEntity
import com.soten.todo.data.repository.TodoRepository
import com.soten.todo.domain.UseCase

internal class UpdateTodoUseCase(
    private val todoRepository: TodoRepository
): UseCase {

    suspend operator fun invoke(todo: TodoEntity) {
        return todoRepository.updateTodo(todo)
    }

}