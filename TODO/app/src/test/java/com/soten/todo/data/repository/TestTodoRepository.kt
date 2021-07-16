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

    override suspend fun updateTodo(todo: TodoEntity): Boolean {
        val foundTodoEntity = todoList.find { it.id == todo.id }
        return if (foundTodoEntity == null) {
            false
        } else {
            this.todoList[todoList.indexOf(foundTodoEntity)] = todo
            true
        }
    }

    override suspend fun getTodoItem(itemId: Long): TodoEntity? {
        return todoList.find { it.id == itemId }
    }

    override suspend fun deleteAll() {
        todoList.clear()
    }
}