package com.soten.todo.data.repository

import com.soten.todo.data.entity.TodoEntity

/**
 * 1. getTodoList
 * 2. insertTodoList
 *
 */
interface TodoRepository {

    suspend fun getTodoList(): List<TodoEntity>

    suspend fun insertTodoList(todoList: List<TodoEntity>)

}