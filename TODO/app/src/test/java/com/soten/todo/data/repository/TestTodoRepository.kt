package com.soten.todo.data.repository

import com.soten.todo.data.entity.TodoEntity
import org.junit.Assert.*

class TestTodoRepository: TodoRepository {

    private val todoList: MutableList<TodoEntity> = mutableListOf()

    override suspend fun getTodoList(): List<TodoEntity> {
        return todoList
    }

    override suspend fun insertTodoList(todoList: List<TodoEntity>) {
        this.todoList.addAll(todoList)
    }
}