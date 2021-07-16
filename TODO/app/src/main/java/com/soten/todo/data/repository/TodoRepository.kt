package com.soten.todo.data.repository

import com.soten.todo.data.entity.TodoEntity

/**
 * 1. getTodoList
 * 2. insertTodoList
 * 3. updateTodo
 */
interface TodoRepository {

    suspend fun getTodoList(): List<TodoEntity>

    suspend fun insertTodoItem(todo: TodoEntity)

    suspend fun insertTodoList(todoList: List<TodoEntity>)

    suspend fun updateTodo(todo: TodoEntity): Boolean

    suspend fun getTodoItem(itemId: Long): TodoEntity?

    suspend fun deleteAll()

    suspend fun deleteTodoItem(id: Long): Boolean

}