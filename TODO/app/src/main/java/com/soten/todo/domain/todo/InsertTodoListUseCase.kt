package com.soten.todo.domain.todo

import com.soten.todo.data.entity.TodoEntity
import com.soten.todo.data.repository.TodoRepository
import com.soten.todo.domain.UseCase

internal class InsertTodoListUseCase(
    private val todoRepository: TodoRepository
): UseCase {

    suspend operator fun invoke(todoList: List<TodoEntity>) {
        return todoRepository.insertTodoList(todoList)
    }

}