package com.soten.todo.data.repository

import com.soten.todo.data.entity.TodoEntity
import com.soten.todo.data.local.db.dao.TodoDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultTodoRepository(
    private val todoDao: TodoDao,
    private val ioDispatcher: CoroutineDispatcher
) : TodoRepository {

    override suspend fun getTodoList(): List<TodoEntity> = withContext(ioDispatcher) {
        todoDao.getAll()
    }

    override suspend fun getTodoItem(itemId: Long): TodoEntity? = withContext(ioDispatcher) {
        todoDao.getById(itemId)
    }

    override suspend fun insertTodoItem(todo: TodoEntity): Long = withContext(ioDispatcher) {
        todoDao.insert(todo)
    }

    override suspend fun insertTodoList(todoList: List<TodoEntity>) = withContext(ioDispatcher) {
        todoDao.insert(todoList)
    }

    override suspend fun updateTodo(todo: TodoEntity) = withContext(ioDispatcher) {
        todoDao.update(todo)
    }

    override suspend fun delete(id: Long) = withContext(ioDispatcher) {
        todoDao.delete(id)
    }

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        todoDao.deleteAll()
    }

}