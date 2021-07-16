package com.soten.todo.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soten.todo.data.entity.TodoEntity
import com.soten.todo.data.local.db.dao.TodoDao

@Database(
    entities = [TodoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TodoDataBase: RoomDatabase() {

    companion object {
        const val DB_NAME = "TodoDataBase.db"
    }

    abstract fun toDoDao(): TodoDao
}