package com.soten.todo.data.repository

import com.soten.todo.data.entity.TodoEntity

interface TodoRepository {

    suspend fun getTodoList(): List<TodoEntity>

    suspend fun insertTodoItem(todo: TodoEntity): Long

    suspend fun insertTodoList(todoList: List<TodoEntity>)

    suspend fun updateTodo(todo: TodoEntity)

    suspend fun getTodoItem(itemId: Long): TodoEntity?

    suspend fun deleteAll()

    suspend fun delete(id: Long)

}