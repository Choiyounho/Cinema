package com.soten.todo.data.repository

import com.soten.todo.data.entity.TodoEntity

object DefaultTodoRepository: TodoRepository {

    override suspend fun getTodoList(): List<TodoEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun insertTodoItem(todo: TodoEntity): Long {
        TODO("Not yet implemented")
    }

    override suspend fun insertTodoList(todoList: List<TodoEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTodo(todo: TodoEntity): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getTodoItem(itemId: Long): TodoEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTodoItem(id: Long): Boolean {
        TODO("Not yet implemented")
    }

}